package com.degressly.proxy.comparator.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Differences {

	Map<String, String> rawDifferences;

	Map<String, String> noise;

	Map<String, String> filteredDifferences;

}
