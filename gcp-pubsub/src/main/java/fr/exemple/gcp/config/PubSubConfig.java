package fr.exemple.gcp.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

import lombok.extern.slf4j.Slf4j;

@EnableIntegration
@Configuration
@Slf4j
class PubSubConfig {

    @Bean
    MessageChannel pubsubInputChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubsubTemplate
    ) {
        var adapter = new PubSubInboundChannelAdapter(pubsubTemplate, "commande-subscription");
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }

    @Bean
    IntegrationFlow integrationFlow(MessageChannel pubsubInputChannel, ObjectMapper objectMapper) {
        return IntegrationFlow.from(pubsubInputChannel)
                .transform(Transformers.objectToString())
                .handle(message -> {
                    try {
                        Map<String, Object> map = objectMapper.readValue(message.getPayload().toString(), Map.class);
                        log.info("Le fichier {} a été créé sur le bucket {}", map.get("name"), map.get("bucket"));

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class).ack();
                })
                .get();
    }
}
