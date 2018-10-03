package com.github.sandonjacobs.stayontopic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopicConfigParser {

    public TopicConfigParser() {

    }
    /**
     * Parses a yaml file containing Club Topicana configuration
     * @param configFileLocation The classpath-relative location of the config file
     * @return
     */
    public Collection<ExpectedTopicConfiguration> parseTopicConfiguration(String configFileLocation) {
        List<Map<String, Object>> map = new org.yaml.snakeyaml.Yaml().load(this.getClass().getClassLoader().getResourceAsStream(configFileLocation));


        return map.stream().map(entry -> {
            ExpectedTopicConfiguration.ExpectedTopicConfigurationBuilder build = new ExpectedTopicConfiguration.ExpectedTopicConfigurationBuilder((String) entry.get("name"));

            if (entry.get("replication-factor") != null) {
                build.withReplicationFactor((Integer) entry.get("replication-factor"));
            }

            if (entry.get("partition-count") != null) {
                build.withReplicationFactor((Integer) entry.get("partition-count"));
            }

            if (entry.get("config") != null) {
                List<Map<String, Object>> config = (List<Map<String, Object>>) entry.get("config");

                config.stream().forEach(configMap -> {
                    Map<String, String> stringifiedConfig = configMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
                    build.withConfig(stringifiedConfig);
                });

            }


            return build.build();

        }).collect(Collectors.toList());
    }
}
