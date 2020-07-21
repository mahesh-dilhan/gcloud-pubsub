package com.gcloud.gcloudpubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
public class OutboundHandler {

    @Value("${pub.sub.topic}")
    private String topicname;

//    @Bean
//    @ServiceActivator(inputChannel = "pubsubOutputChannel")
//    public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
//        return new PubSubMessageHandler(pubsubTemplate, topicname);
//    }
//
//    //way to capture message from external parties,
//    // same message will eligible for publish to pubsub
//    @MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
//    interface PubsubOutboundGateway {
//         void sendToPubsub(String text);
//    }
//

    @MessagingGateway(defaultRequestChannel = "OutboundChannel")
    interface ExternalMessageDeligator{
        void sendToPubsub(String payload);
    }

    //conume message in message channel and mediate message to pubsub
    @Bean
    @ServiceActivator(inputChannel = "OutboundChannel")
    public MessageHandler messageDeligator(PubSubTemplate pubSubTemplate){
        return new PubSubMessageHandler(pubSubTemplate,topicname);
    }
}
