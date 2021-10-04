package com.accenture.filereadersafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.aop.AbstractMessageSourceAdvice;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class StopPollingAdvice extends AbstractMessageSourceAdvice {

    @Lazy
    @Qualifier("controlBus")
    @Autowired
    private MessageChannel controlBusChannel;

    @Override
    public boolean beforeReceive(MessageSource<?> source){
        return super.beforeReceive(source);
    }

    @Override
    public Message<?> afterReceive(Message<?> result, MessageSource<?> source) {
        //Detiene el poller si el último mensaje recibido tiene payload "No hay más mensajes"
        if(result.getPayload().equals("No hay más mensajes")){
            Message operation = MessageBuilder.withPayload("@fileInboundChannelAdapter.stop()").build();
            controlBusChannel.send(operation);
        }
        return result;
    }
}
