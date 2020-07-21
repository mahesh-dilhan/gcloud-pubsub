package com.gcloud.gcloudpubsub;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InboundHandler {
    @Value("${pub.sub.subscription}")
    private String subscription;

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(@Qualifier("InputChannel") MessageChannel inputChannel, PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscription);
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }
    @Bean
    public MessageChannel InputChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "InputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            log.info("Received Message ..! Payload: {}", message);
            BasicAcknowledgeablePubsubMessage acknowledgeablePubsubMessage = message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);
            acknowledgeablePubsubMessage.ack();
        };
    }
}
