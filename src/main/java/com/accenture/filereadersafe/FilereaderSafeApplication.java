package com.accenture.filereadersafe;

import com.accenture.filereadersafe.service.FileReaderService;
import com.accenture.filereadersafe.service.StopPollingAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@EnableBinding(Source.class)
@SpringBootApplication
public class FilereaderSafeApplication {

	@Autowired
	private FileReaderService service;

	private String filename = "casos_covid19.cvs";

	public static void main(String[] args) {
		SpringApplication.run(FilereaderSafeApplication.class, args);
	}

	@Bean
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller("fileReadingMessageSourcePollerMetadata"))
	public MessageSource<String> fileReader(){
		return () -> service.readFile(filename);
	}

	@Bean
	public IntegrationFlow controlBusFlow(){
		return IntegrationFlows.from("controlBus")
				.controlBus()
				.get();
	}

	@Bean
	public PollerMetadata fileReadingMessageSourcePollerMetadata(StopPollingAdvice stopPollingAdvice){
		PollerMetadata meta = new PollerMetadata();
		//Puede recibir una unidad específica como segundo parámetro
		meta.setTrigger(new PeriodicTrigger(10, TimeUnit.MICROSECONDS));
		meta.setAdviceChain(Collections.singletonList(stopPollingAdvice));
		meta.setMaxMessagesPerPoll(1);
		return meta;
	}
}
