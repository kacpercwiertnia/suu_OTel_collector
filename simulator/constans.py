TEST_SENSORS = [{'label': "Kraków", 'latitude': 50.0619, 'longitude': 19.9359}]
ALL_SENSORS = [{'label': "Kraków", 'latitude': 50.0619, 'longitude': 19.9359},
           {'label': "Warszawa", 'latitude': 52.2319, 'longitude': 21.0067},
           {'label': "Katowice", 'latitude': 50.2598, 'longitude': 19.0215},
           {'label': "Wrocław", 'latitude': 51.1089, 'longitude': 17.0326},
           {'label': "Gdańsk", 'latitude': 54.3706, 'longitude': 18.6129},
           {'label': "Łódź", 'latitude': 51.7592, 'longitude': 19.4550},
           {'label': "Poznań", 'latitude': 52.4064, 'longitude': 16.9252},
           {'label': "Szczecin", 'latitude': 53.4285, 'longitude': 14.5528},
           {'label': "Lublin", 'latitude': 51.2465, 'longitude': 22.5684},
           {'label': "Białystok", 'latitude': 53.1325, 'longitude': 23.1688},
           {'label': "Gdynia", 'latitude': 54.5189, 'longitude': 18.5305},
           {'label': "Bydgoszcz", 'latitude': 53.1235, 'longitude': 18.0084},
           {'label': "Częstochowa", 'latitude': 50.8118, 'longitude': 19.1203},
           {'label': "Radom", 'latitude': 51.4027, 'longitude': 21.1471},
           {'label': "Toruń", 'latitude': 53.0138, 'longitude': 18.5984},
           {'label': "Kielce", 'latitude': 50.8661, 'longitude': 20.6286},
           {'label': "Rzeszów", 'latitude': 50.0412, 'longitude': 21.9991},
           {'label': "Gliwice", 'latitude': 50.2945, 'longitude': 18.6714},
           {'label': "Zabrze", 'latitude': 50.3249, 'longitude': 18.7857},
           {'label': "Olsztyn", 'latitude': 53.7761, 'longitude': 20.4752},
           {'label': "Opole", 'latitude': 50.6751, 'longitude': 17.9213},
           {'label': "Elbląg", 'latitude': 54.1561, 'longitude': 19.4045},
           {'label': "Płock", 'latitude': 52.5463, 'longitude': 19.7065},
           {'label': "Zielona Góra", 'latitude': 51.9356, 'longitude': 15.5062},
           {'label': "Wałbrzych", 'latitude': 50.7710, 'longitude': 16.2843}]
COUNTRIES = ["Poland", "Germany", "France", "Spain", "Italy", "Great Britain", "Netherlands", "Belgium", "Austria", "Sweden", "Norway", "Denmark", "Finland", "Switzerland", "Czech Republic", "Hungary", "Greece", "Portugal", "Ireland"]
OPERATORS = ["PL", "DE", "FR", "ES", "IT", "GB", "NL", "BE", "AT", "SE", "NO", "DK", "FI", "CH", "CZ", "HU", "GR", "PT", "IE"]
IDENTIFICATION_LABELS = ["Dark Red", "Red", "Gold", "Yellow", "Dark Green", "Green", "Aqua", "Dark Aqua", "Dark Blue", "Blue", "Light Purple", "Dark Purple", "White", "Gray", "Dark Gray", "Black"]
MODELS = ["DJI Mini 4 Pro", "DJI Mini 3", "DJI Air 3", "DJI Mavic 3 Pro", "DJI Air 2S rival", "DJI Mavic 3 Classic", "DJI Phantom 4 Pro V2.0", "DJI Mavic Air 2", "DJI FPV Combo", "DJI Inspire 2", "DJI Matrice 300 RTK", "DJI Mini 2", "DJI Mavic 2 Pro", "DJI Spark", "DJI Mavic Air", "DJI Phantom 4", "DJI Matrice 200", "DJI Mavic 2 Zoom", "DJI Phantom 3 Professional", "DJI Phantom 3 Standard", "DJI Matrice 600 Pro"]
CONFIG_CONSTRAINTS = {'NUMBER_OF_DRONES': {'min_value': 10, 'max_value': 100},
                      'NUMBER_OF_SENSORS': {'min_value': 1, 'max_value': 1},
                      'MINIMUM_FLIGHT_LENGTH': {'min_value': 10, 'max_value': 100},
                      'MAXIMUM_FLIGHT_LENGTH': {'min_value': 10, 'max_value': 100},
                      'MINIMUM_FLIGHT_STOP_LENGTH': {'min_value': 1, 'max_value': 20},
                      'MAXIMUM_FLIGHT_STOP_LENGTH': {'min_value': 1, 'max_value': 20}}
DEFAULT_CONFIG = {'NUMBER_OF_DRONES': 10,
          'NUMBER_OF_SENSORS': 1,
          'MINIMUM_FLIGHT_LENGTH': 10,
          'MAXIMUM_FLIGHT_LENGTH': 15,
          'MINIMUM_FLIGHT_STOP_LENGTH': 5,
          'MAXIMUM_FLIGHT_STOP_LENGTH': 10}
MAX_FLIGHT_ALT = 120
SENSOR_RANGE = 0.01