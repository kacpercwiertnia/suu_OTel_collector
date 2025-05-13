package com.example.backend.simulatorIntegration;

import com.example.backend.simulatorIntegration.configuration.SharedFolderConfig;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FilePathProvider {
    private final SharedFolderConfig config;

    public FilePathProvider(SharedFolderConfig config) {
        this.config = config;
    }

    public String getFilePath(String filename){
        StringBuilder filePath = new StringBuilder();

        filePath.append(new File(System.getProperty("user.dir")).getParent());
        filePath.append(config.getPath());
        filePath.append(filename);

        return filePath.toString();
    }
}
