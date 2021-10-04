package com.accenture.filereadersafe;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("filereaderSafe")
@Validated
public class FilereaderSafeProperties {

    /*
    * Nombre del archivo a descargar
    * */
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
