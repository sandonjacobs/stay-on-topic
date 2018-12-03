package com.github.sandonjacobs.stayontopic.core;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

public class TopicCreator {

    private final String bootstrapServer;

    public TopicCreator(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public CreationResult createTopics(Set<String> topics, Collection<ExpectedTopicConfiguration> expectedTopicConfiguration) {

        CreationResult.CreationResultBuilder builder = CreationResult.builder();

        Stream<ExpectedTopicConfiguration> topicsToCreate = expectedTopicConfiguration.stream().filter(exp -> topics.contains(exp.getTopicName()));

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        try (AdminClient adminClient = AdminClient.create(props)) {
            topicsToCreate.forEach(cfg -> {
                NewTopic topic = new NewTopic(cfg.getTopicName(), cfg.getPartitions().count(), (short) cfg.getReplicationFactor().count());
                topic.configs(cfg.getProps());

                adminClient.createTopics(Collections.singletonList(topic), new CreateTopicsOptions().timeoutMs(10000)).values().values().stream().forEach(f -> {
                    if (f.isDone()) builder.createdTopic(topic.name());
                    else builder.failedTopic(topic.name());
                });
            });
        }
        return builder.build();
    }
}
