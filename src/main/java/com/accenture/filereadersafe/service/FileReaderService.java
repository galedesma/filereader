package com.accenture.filereadersafe.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class FileReaderService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private AmazonS3 s3Client;

    private Source source;

    public GenericMessage<String> readFile(String filename){
        int contador = 0;
        S3Object s3Object = s3Client.getObject(bucketName, filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try{
            while((line = csvReader.readLine()) != null){
                source.output().send(new GenericMessage<>(line + " " + contador));
                contador++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return new GenericMessage<>("No hay más mensajes");
    }
}

