package com.github.sandonjacobs.stayontopic;

import java.util.*;

public class ComparisonResult {

    private final Set<String> missingTopics;

    private final Map<String, Comparison<Integer>> mismatchingReplicationFactor;

    private final Map<String, Comparison<Integer>> mismatchingPartitionCount;

    private final Map<String, Collection<Comparison<String>>> mismatchingConfiguration;


    ComparisonResult(Set<String> missingTopics, Map<String, Comparison<Integer>> mismatchingReplicationFactor, Map<String, Comparison<Integer>> mismatchingPartitionCount, Map<String, Collection<Comparison<String>>> mismatchingConfiguration) {
        this.missingTopics = missingTopics;
        this.mismatchingReplicationFactor = mismatchingReplicationFactor;
        this.mismatchingPartitionCount = mismatchingPartitionCount;
        this.mismatchingConfiguration = mismatchingConfiguration;

    }

    public boolean ok(){
        return missingTopics.isEmpty() && mismatchingReplicationFactor.isEmpty() && mismatchingPartitionCount.isEmpty() && mismatchingConfiguration.isEmpty();
    }

    public Set<String> getMissingTopics() {
        return missingTopics;
    }

    public Map<String, Comparison<Integer>> getMismatchingReplicationFactor() {
        return mismatchingReplicationFactor;
    }

    public Map<String, Comparison<Integer>> getMismatchingPartitionCount() {
        return mismatchingPartitionCount;
    }

    public Map<String, Collection<Comparison<String>>> getMismatchingConfiguration() {
        return mismatchingConfiguration;
    }


    @Override
    public String toString() {
        return "ComparisonResult{" +
                "missingTopics=" + missingTopics +
                ", mismatchingReplicationFactor=" + mismatchingReplicationFactor +
                ", mismatchingPartitionCount=" + mismatchingPartitionCount +
                ", mismatchingConfiguration=" + mismatchingConfiguration +
                '}';
    }

    public static class Comparison<T> {
        private final String topicName;
        private final T actualValue;
        private final T expectedValue;
        private final String property;

        private Comparison(String topicName, String description,  T actual, T expected) {
            this.topicName = topicName;
            this.actualValue = actual;
            this.expectedValue = expected;
            this.property = description;
        }

        private Comparison(String topicName,  T actual, T expected) {
            this(topicName, null, actual, expected);
        }

        public String getTopicName() {
            return topicName;
        }

        public T getActualValue() {
            return actualValue;
        }

        public T getExpectedValue() {
            return expectedValue;
        }

        public String getProperty() {
            return property;
        }

        @Override
        public String toString() {
            return "Comparison{" +
                    "topicName='" + topicName + '\'' +
                    ", property='" + property + '\'' +
                    ", actualValue=" + actualValue +
                    ", expectedValue=" + expectedValue +
                    '}';
        }
    }


    public static class ComparisonResultBuilder {
        private final Set<String> missingTopics = new HashSet<>();
        private final Map<String, ComparisonResult.Comparison<Integer>> mismatchingReplicationFactor = new HashMap<>();
        private final Map<String, ComparisonResult.Comparison<Integer>> mismatchingPartitionCount = new HashMap<>();
        private final Map<String, Collection<ComparisonResult.Comparison<String>>> mismatchingConfiguration = new HashMap<>();



        public ComparisonResultBuilder addMissingTopic(String missingTopic) {
            missingTopics.add(missingTopic);
            return this;
        }

        public ComparisonResultBuilder addMismatchingReplicationFactor(String topicName, int expected, int actual) {
            this.mismatchingReplicationFactor.put(topicName, new Comparison<>(topicName, "replication factor",  actual, expected));
            return this;
        }

        public ComparisonResultBuilder addMismatchingPartitionCount(String topicName, int expected, int actual) {
            this.mismatchingPartitionCount.put(topicName, new Comparison<>(topicName, "partition count", actual, expected));
            return this;
        }

        public ComparisonResultBuilder addMismatchingConfiguration(String topicName, String property,  String expected, String actual) {

            this.mismatchingConfiguration.putIfAbsent(topicName, new ArrayList<>());
            this.mismatchingConfiguration.get(topicName).add(new Comparison<>(topicName, property, actual, expected));

            return this;
        }





        public ComparisonResult build() {
            return new ComparisonResult(missingTopics, mismatchingReplicationFactor, mismatchingPartitionCount, mismatchingConfiguration);
        }
    }


}

