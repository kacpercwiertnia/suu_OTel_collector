package com.example.backend.simulatorIntegration;

import com.example.backend.simulatorIntegration.model.DroneFromSimulator;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DroneRecordsCsvReader {
    public List<DroneFromSimulator> parseCsv(String path){
        List<DroneFromSimulator> drones = new ArrayList<>();

        try (Reader reader = new FileReader(path)){
            CsvToBean<DroneFromSimulator> csvToBean = new CsvToBeanBuilder<DroneFromSimulator>(reader)
                    .withType(DroneFromSimulator.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            drones = csvToBean.parse();

            log.info("Correctly read previous drones records");
        }catch(IOException ex){
            log.error("Could not connect to shared folder. Reason: " + ex);
        }

        return drones;
    }
}
