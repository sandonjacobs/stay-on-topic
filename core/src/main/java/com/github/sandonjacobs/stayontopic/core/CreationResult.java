package com.github.sandonjacobs.stayontopic.core;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Set;

@Data
@Builder
public class CreationResult {
    @Singular private Set<String> createdTopics;
    @Singular private Set<String> failedTopics;
}
