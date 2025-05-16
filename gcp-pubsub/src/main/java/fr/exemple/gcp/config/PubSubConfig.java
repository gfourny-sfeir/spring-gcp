package fr.exemple.gcp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.MessageChannel;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;

import fr.exemple.gcp.MessageHandler;

@EnableIntegration
@Configuration
class PubSubConfig {

    @Bean
    MessageChannel pubsubInputChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    PubSubInboundChannelAdapter messageChannelAdapter(
            @Qualifier("pubsubInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubsubTemplate,
            ApplicationProperties applicationProperties
    ) {
        var adapter = new PubSubInboundChannelAdapter(pubsubTemplate, applicationProperties.subscriptionName());
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }

    @Bean
    IntegrationFlow integrationFlow(MessageChannel pubsubInputChannel, MessageHandler messageHandler) {

        return IntegrationFlow.from(pubsubInputChannel)
                .transform(Transformers.objectToString())
                .handle(messageHandler::handle)
                .get();
    }
}
