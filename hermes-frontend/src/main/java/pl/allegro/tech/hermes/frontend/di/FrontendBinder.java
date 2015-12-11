package pl.allegro.tech.hermes.frontend.di;

import org.I0Itec.zkclient.ZkClient;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import pl.allegro.tech.hermes.domain.topic.schema.SchemaRepository;
import pl.allegro.tech.hermes.domain.topic.schema.SchemaRepositoryListFactory;
import pl.allegro.tech.hermes.frontend.cache.topic.TopicsCache;
import pl.allegro.tech.hermes.frontend.cache.topic.zookeeper.ZookeeperTopicsCacheFactory;
import pl.allegro.tech.hermes.frontend.producer.BrokerMessageProducer;
import pl.allegro.tech.hermes.frontend.producer.kafka.KafkaBrokerMessageProducerFactory;
import pl.allegro.tech.hermes.frontend.producer.kafka.KafkaMessageProducerFactory;
import pl.allegro.tech.hermes.frontend.producer.kafka.Producers;
import pl.allegro.tech.hermes.frontend.publishing.MessageContentTypeEnforcer;
import pl.allegro.tech.hermes.frontend.publishing.MessagePublisher;
import pl.allegro.tech.hermes.frontend.publishing.PublishingServlet;
import pl.allegro.tech.hermes.frontend.publishing.metadata.DefaultHeadersPropagator;
import pl.allegro.tech.hermes.frontend.publishing.metadata.HeadersPropagator;
import pl.allegro.tech.hermes.frontend.publishing.metadata.MetadataAddingMessageConverter;
import pl.allegro.tech.hermes.frontend.server.HermesServer;
import pl.allegro.tech.hermes.frontend.services.HealthCheckService;
import pl.allegro.tech.hermes.frontend.validator.AvroTopicMessageValidator;
import pl.allegro.tech.hermes.frontend.validator.JsonTopicMessageValidator;
import pl.allegro.tech.hermes.frontend.validator.MessageValidators;
import pl.allegro.tech.hermes.frontend.validator.TopicMessageValidator;
import pl.allegro.tech.hermes.frontend.validator.TopicMessageValidatorListFactory;
import pl.allegro.tech.hermes.frontend.zk.ZkClientFactory;
import pl.allegro.tech.hermes.tracker.frontend.NoOperationPublishingTracker;
import pl.allegro.tech.hermes.tracker.frontend.PublishingMessageTracker;

import javax.inject.Singleton;
import java.util.List;

public class FrontendBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindSingleton(HermesServer.class);
        bindSingleton(PublishingServlet.class);
        bindSingleton(MessageValidators.class);

        bindSingleton(HealthCheckService.class);
        bind(DefaultHeadersPropagator.class).to(HeadersPropagator.class).in(Singleton.class);

        bindFactory(KafkaMessageProducerFactory.class).to(Producers.class).in(Singleton.class);
        bindFactory(KafkaBrokerMessageProducerFactory.class).to(BrokerMessageProducer.class).in(Singleton.class);
        bindFactory(ZkClientFactory.class).to(ZkClient.class).in(Singleton.class);
        bindSingleton(PublishingMessageTracker.class);
        bindSingleton(NoOperationPublishingTracker.class);
        bindFactory(ZookeeperTopicsCacheFactory.class).to(TopicsCache.class).in(Singleton.class);
        bindSingleton(MessagePublisher.class);
        bindSingleton(MessageContentTypeEnforcer.class);
        bindSingleton(JsonTopicMessageValidator.class);
        bindSingleton(AvroTopicMessageValidator.class);
        bindSingleton(MetadataAddingMessageConverter.class);
        bindFactory(TopicMessageValidatorListFactory.class).in(Singleton.class).to(new TypeLiteral<List<TopicMessageValidator>>() {});
        bindFactory(SchemaRepositoryListFactory.class).in(Singleton.class).to(new TypeLiteral<List<SchemaRepository>>() {});
    }

    private <T> void bindSingleton(Class<T> clazz) {
        bind(clazz).in(Singleton.class).to(clazz);
    }
}
