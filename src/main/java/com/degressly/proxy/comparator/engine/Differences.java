package com.degressly.proxy.comparator.engine;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Differences {

	Map<String, String> rawDifferences;

	Map<String, String> noise;

	Map<String, String> filteredDifferences;

}
