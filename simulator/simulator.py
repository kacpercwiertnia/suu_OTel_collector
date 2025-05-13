
import psutil
from opentelemetry import metrics
from opentelemetry.sdk.metrics import MeterProvider
from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
from opentelemetry.exporter.otlp.proto.http.metric_exporter import OTLPMetricExporter

import numpy as np
import pandas as pd
from datetime import datetime
import random
import string
import time
import uuid
import pika
import constans as const
from os import getenv

def parse_config_file(file):
    config = const.DEFAULT_CONFIG.copy()

    file_context = file.read()
    lines = file_context.splitlines()

    for line in lines:
        line = line.replace(" ", "")

        if len(line) == 0 or "=" not in line:
            continue

        parameter_name, parameter_value = line.split("=")
        parameter_value = int(parameter_value)

        if parameter_name not in const.CONFIG_CONSTRAINTS:
            raise Exception(f"{parameter_name} is invalid parameter")

        if parameter_value < const.CONFIG_CONSTRAINTS[parameter_name]['min_value'] or parameter_value > const.CONFIG_CONSTRAINTS[parameter_name]['max_value']:
            raise Exception(f"Value for {parameter_name} should be between {const.CONFIG_CONSTRAINTS[parameter_name]['min_value']} and {const.CONFIG_CONSTRAINTS[parameter_name]['max_value']}")

        config[parameter_name] = parameter_value
    
    if const.DEFAULT_CONFIG['MAXIMUM_FLIGHT_LENGTH'] < const.DEFAULT_CONFIG['MINIMUM_FLIGHT_LENGTH']:
        raise Exception("MINIMUM_FLIGHT_LENGTH cannot be greater then MAXIMUM_FLIGHT_LENGTH")
    
    if const.DEFAULT_CONFIG['MAXIMUM_FLIGHT_STOP_LENGTH'] < const.DEFAULT_CONFIG['MINIMUM_FLIGHT_STOP_LENGTH']:
        raise Exception("MINIMUM_FLIGHT_STOP_LENGTH cannot be greater then MAXIMUM_FLIGHT_STOP_LENGTH")
    
    return config

def generate_random_registration_number():
    letters = random.choices(string.ascii_uppercase, k=3)
    digits = random.choices(string.digits, k=3)
    
    random_string = letters + digits

    return ''.join(random_string)

def generate_drone_data(i, drones_data):
    country = random.choice(const.COUNTRIES)
    operator = random.choice(const.OPERATORS)
    identification = random.randint(1, 16)
    identification_label = const.IDENTIFICATION_LABELS[identification-1]
    model = random.choice(const.MODELS)
    if i == 0:
        registration_number = "TEST123"
    else:
        registration_number = generate_random_registration_number()
    sign = operator + registration_number

    drones_data[i] = {'country': country, 'operator': operator, 'identification': identification, 'identification_label': identification_label, 'model': model, 'registration_number': registration_number, 'sign': sign, 'flight': None}

def calculate_heading(latitude_before, longitude_before, latitude_after, longitude_after):
    latitude_before_radians = np.radians(latitude_before)
    latitude_after_radians = np.radians(latitude_after)

    longitude_difference_radians = np.radians(longitude_after - longitude_before)

    x = np.sin(longitude_difference_radians) * np.cos(latitude_after_radians)
    y = np.cos(latitude_before_radians) * np.sin(latitude_after_radians) - (np.sin(latitude_before_radians) * np.cos(latitude_after_radians) * np.cos(longitude_difference_radians))

    initial_heading = np.arctan2(x, y)
    initial_heading = np.degrees(initial_heading)

    compass_heading = (initial_heading + 360) % 360

    return compass_heading

def convert_to_dms(lat, lon):
    lat_deg = int(lat)
    lat_min = int((lat - lat_deg) * 60)
    lat_sec = (lat - lat_deg - lat_min / 60) * 3600

    lon_deg = int(lon)
    lon_min = int((lon - lon_deg) * 60)
    lon_sec = (lon - lon_deg - lon_min / 60) * 3600

    lat_hemisphere = 'N' if lat >= 0 else 'S'
    lon_hemisphere = 'E' if lon >= 0 else 'W'

    lat_str = f"{abs(lat_deg):02}{abs(lat_min):02}{abs(lat_sec):02.0f}{lat_hemisphere}"
    lon_str = f"{abs(lon_deg):03}{abs(lon_min):02}{abs(lon_sec):02.0f}{lon_hemisphere}"

    return lat_str, lon_str

def haversine_distance(lat1, lon1, lat2, lon2):
    R = 6371.0

    dlat = np.radians(lat2 - lat1)
    dlon = np.radians(lon2 - lon1)

    a = np.sin(dlat / 2)**2 + np.cos(np.radians(lat1)) * np.cos(np.radians(lat2)) * np.sin(dlon / 2)**2
    c = 2 * np.arctan2(np.sqrt(a), np.sqrt(1 - a))

    distance_2d = R * c

    return distance_2d

def generate_flight_ticks(starting_latitude, starting_longitude, is_test_drone):
    number_of_points = random.randint(CONFIG['MINIMUM_FLIGHT_LENGTH'], CONFIG['MAXIMUM_FLIGHT_LENGTH'])

    if is_test_drone:
            number_of_points = 1000000
            
    latitudes = [0 for _ in range(number_of_points)]
    longitudes = [0 for _ in range(number_of_points)]
    headings = np.zeros(number_of_points)
    altitudes = np.zeros(number_of_points)
    speeds = np.zeros(number_of_points)
    fuel = np.zeros(number_of_points)

    scale_factor_geo = 0.0005  
    alpha = 2.5
    time = np.linspace(0, np.pi, number_of_points)
    shaping_factor = np.sin(time)
    alt_up_scale = 90 / (number_of_points ** 0.5)
    alt_down_scale = 55 / (number_of_points ** 0.5)
    midpoint = number_of_points // 2 

    lat_before = starting_latitude
    lon_before = starting_longitude
    alt_before = 1
    fuel_before = random.uniform(80, 100)
    fuel_distance_multiplier = 38
    fuel_elevation_gain_multiplier = 1.5

    for i in range(number_of_points):
        step_length = np.random.pareto(alpha)
        theta = np.random.uniform(0, 2 * np.pi)
        
        latitudes[i] = lat_before + step_length * np.cos(theta) * scale_factor_geo
        longitudes[i] = lon_before + step_length * np.sin(theta) * scale_factor_geo
        headings[i] = calculate_heading(lat_before, lon_before, latitudes[i], longitudes[i])

        if i < midpoint:
            altitudes[i] = min(const.MAX_FLIGHT_ALT, alt_before + np.abs(step_length * shaping_factor[i]) * alt_up_scale)
        else:
            altitudes[i] = max(1, alt_before - np.abs(step_length * shaping_factor[i]) * alt_down_scale)

        if i == number_of_points-1:
            altitudes[i] = 1

        if is_test_drone:
            altitudes[i] = 500

        distance_2d = haversine_distance(lat_before, lon_before, latitudes[i], longitudes[i])
        distance_3d = np.sqrt(distance_2d**2 + ((altitudes[i] - alt_before)/1000)**2)
        
        speeds[i] = distance_3d * 1800

        if altitudes[i] > alt_before:
            fuel[i] = fuel_before - distance_3d*fuel_distance_multiplier*fuel_elevation_gain_multiplier
        else:
            fuel[i] = fuel_before - distance_3d*fuel_distance_multiplier

        if is_test_drone:
            fuel[i] = 50

        lat_before = latitudes[i]
        lon_before = longitudes[i]
        alt_before = altitudes[i]
        fuel_before = fuel[i]

        latitudes[i], longitudes[i] = convert_to_dms(latitudes[i], longitudes[i])

        if fuel[i] <= 1:
            altitudes[i] = 1
            fuel[i] = 1
            return i+1, latitudes[:i+1], longitudes[:i+1], headings[:i+1], altitudes[:i+1], fuel[:i+1], speeds[:i+1]

    return number_of_points, latitudes, longitudes, headings, altitudes, fuel, speeds

def random_point_in_sensor_range(sensor_lat, sensor_lon):
    theta = random.uniform(0, 2 * np.pi)
    
    r = const.SENSOR_RANGE * np.sqrt(random.uniform(0, 1))
    
    delta_latitude = r * np.cos(theta)
    delta_longitude = r * np.sin(theta) / np.cos(np.radians(sensor_lat))
    
    new_latitude = sensor_lat + delta_latitude
    new_longitude = sensor_lon + delta_longitude
    
    return new_latitude, new_longitude

def generate_drone_flight_data(is_test_drone):
    sensor = random.choice(SENSORS)
    starting_latitude, starting_longitude = random_point_in_sensor_range(sensor['latitude'], sensor['longitude'])

    number_of_points, latitudes, longitudes, headings, altitudes, fuel, speeds = generate_flight_ticks(starting_latitude, starting_longitude, is_test_drone)
    
    return {'number_of_points': number_of_points, 'sensor': sensor, 'latitudes': latitudes, 'longitudes': longitudes, 'headings': headings, 'speeds': speeds, 'altitudes': altitudes, 'fuel': fuel}

def generate_drones():
    drones_data = [None for _ in range(CONFIG['NUMBER_OF_DRONES'])]

    generate_drone_data(0, drones_data)

    for i in range(1, CONFIG['NUMBER_OF_DRONES']):
        generate_drone_data(i, drones_data)
    
    return drones_data

def prepare_single_flight_tick(index, drone_data, flight_data, file_name, day_date, day_time, flag):
    sensor_lat, sensor_lon = convert_to_dms(flight_data['sensor']['latitude'],flight_data['sensor']['longitude'])

    return pd.DataFrame([{
            "Filename":file_name,
            "Server":"Server1",
            "Date":day_date,
            "Time":day_time,
            "Flag":flag,
            "Id":"0000001",
            "IdExt": str(uuid.uuid4()),
            "Latitude":flight_data['latitudes'][index],
            "Longitude":flight_data['longitudes'][index],
            "Heading":int(flight_data['headings'][index]),
            "Speed":int(flight_data['speeds'][index]),
            "Altitude":int(flight_data['altitudes'][index]),
            "Country":drone_data['country'],
            "Operator":drone_data['operator'],
            "Identification":int(drone_data['identification']),
            "IdentificationLabel":drone_data['identification_label'],
            "Model":drone_data['model'],
            "RegistrationNumber":drone_data['registration_number'],
            "Sign":drone_data['sign'],
            "Type":"Airborne",
            "Fuel":int(flight_data['fuel'][index]),
            "Signal":"Mode S",
            "Frequency":1000,
            "SensorLat":sensor_lat,
            "SensorLon":sensor_lon,
            "SensorLabel":flight_data['sensor']['label'],
            "Notes":"Notatka numer 1",
            "Ext1":"Parametr ext 1",
            "Ext2":"Parametr ext 2",
            "Ext3":"Parametr ext 3",
            "Ext4":"Parametr ext 4",
            "Ext5":"Parametr ext 5",
            "Ext6":"Parametr ext 6"
        }])

def init_connection():
    global channel

    rabbit_host = getenv("RABBITMQ_HOST", "localhost")
    credentials = pika.PlainCredentials('guest','guest')
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=rabbit_host, credentials=credentials))
    channel = connection.channel()

    channel.exchange_declare('fileExchange', durable=True, exchange_type='topic')

    channel.queue_declare(queue='fileQueue')
    channel.queue_bind(queue='fileQueue', exchange='fileExchange', routing_key='fileCreation')

def close_connection():
    global channel

    channel.close()

if __name__ == "__main__":
    try:
        file = open("config.txt")

        CONFIG = parse_config_file(file)
        print("Loaded custom conifg")

        file.close()
    except OSError as ex:
        print(f"Failed to load custom config: {ex}")
        print("Using default config instead")
        CONFIG = const.DEFAULT_CONFIG
    except Exception as ex:
        print(f"Failed to load custom config: {ex}")
        print("Using default config instead")
        CONFIG = const.DEFAULT_CONFIG
        file.close()

    channel = None

    init_connection()

    metric_exporter = OTLPMetricExporter(endpoint="http://localhost:4318/v1/metrics")
    reader = PeriodicExportingMetricReader(metric_exporter)
    metrics.set_meter_provider(MeterProvider(metric_readers=[reader]))
    meter = metrics.get_meter(__name__)

    def cpu_callback(options):
        cpu = psutil.cpu_percent(interval=1)
        print(f"[METRIC] CPU usage: {cpu}%")
        return [metrics.Observation(cpu, {})]

    def mem_callback(options):
        mem = psutil.virtual_memory().percent
        print(f"[METRIC] Memory usage: {mem}%")
        return [metrics.Observation(mem, {})]

    meter.create_observable_gauge(
        name="system_cpu_usage_percent",
        callbacks=[cpu_callback],
        unit="%",
        description="CPU usage percentage",
    )

    meter.create_observable_gauge(
        name="system_memory_usage_percent",
        callbacks=[mem_callback],
        unit="%",
        description="Memory usage percentage",
    )

    print("Simulator started...")

    drones = generate_drones()
    drones_stop_ticks = [0 for _ in range(CONFIG['NUMBER_OF_DRONES'])]
    drones_tick_indexes = [0 for _ in range(CONFIG['NUMBER_OF_DRONES'])]
    SENSORS = random.choices(const.TEST_SENSORS, k=CONFIG['NUMBER_OF_SENSORS'])

    while True:
        file_name = "File"+str(uuid.uuid4())
        now = datetime.now()
        day_date = now.strftime("%d%m%Y")
        day_time = now.strftime("%H:%M:%S.%f")[:-2]
        drones_dfs = []

        for i in range(CONFIG['NUMBER_OF_DRONES']):
            if drones_stop_ticks[i] != 0:
                drones_stop_ticks[i] -= 1
                continue
            
            index = drones_tick_indexes[i]
            drones_tick_indexes[i] += 1
            flag = "UPD"

            if index == 0:
                flag = "BEG"
                if i == 0:
                    drones[i]['flight'] = generate_drone_flight_data(True)
                else:    
                    drones[i]['flight'] = generate_drone_flight_data(False)
            elif index == drones[i]['flight']['number_of_points'] - 1:
                flag = "DROP"
                drones_tick_indexes[i] = 0
                drones_stop_ticks[i] = random.randint(CONFIG['MINIMUM_FLIGHT_STOP_LENGTH'], CONFIG['MAXIMUM_FLIGHT_STOP_LENGTH'])

            drones_dfs.append(prepare_single_flight_tick(index, drones[i], drones[i]['flight'], file_name, day_date, day_time, flag))

        if(len(drones_dfs) > 0):
            combined_data = pd.concat(drones_dfs)
            combined_data.to_csv(f"../shared_directory/{file_name}.csv", sep=',', index=False)
            channel.basic_publish(exchange='fileExchange', routing_key='fileCreation', body=f"{file_name}.csv")

            print(f"New data saved to /shared_directory/{file_name}.csv")

            time.sleep(2)
    
    close_connection()
