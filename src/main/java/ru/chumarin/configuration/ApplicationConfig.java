package ru.chumarin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *  Конфигурация приложения
 */
@Configuration
@ConfigurationProperties("server")
public class ApplicationConfig {

    /**
     * Размерность сгенерированных файлов, по умолчанию 1Гб
     */
    @Value("${fileSize:1}")
    private Double fileSize;

    /**
     * Путь в папке в которой размещены сгенерированные файлы
     */
    @Value("${fileDir: src/main/resources/file}")
    private String fileDir;

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
