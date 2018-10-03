package com.github.sandonjacobs.stayontopic;


public class MismatchedTopicConfigException extends RuntimeException {


    private final ComparisonResult result;

    public MismatchedTopicConfigException(ComparisonResult result){
        super("Topic configuration does not match specification: " +result.toString());
        this.result = result;
    }

    public ComparisonResult getResult() {
        return result;
    }
}