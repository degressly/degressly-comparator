package com.degressly.proxy.comparator.engine;

import com.degressly.proxy.comparator.dto.Observation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import java.util.*;

@UtilityClass
public class DiffEngineNew {

	public static final ObjectMapper objectMapper = new ObjectMapper();

	public static Pair<Differences, Differences> getNonDeterministicDifferences(Observation observation) {

		Differences requestDifferences = getRequestDifferences(observation);
		Differences responseDifferences = getResponseDifferences(observation);

		return Pair.of(responseDifferences, requestDifferences);

	}

	private static Differences getResponseDifferences(Observation observation) {
		Map<String, Object> primaryResult = convertToFlatMap(
				objectMapper.convertValue(observation.getPrimaryResult(), new TypeReference<>() {
				}));
		Map<String, Object> secondaryResult = convertToFlatMap(
				objectMapper.convertValue(observation.getSecondaryResult(), new TypeReference<>() {
				}));
		Map<String, Object> candidateResult = convertToFlatMap(
				objectMapper.convertValue(observation.getCandidateResult(), new TypeReference<>() {
				}));

		Map<String, String> rawDifferences = getDifferences("primary", primaryResult, "candidate", candidateResult);
		Map<String, String> noise = getDifferences("primary", primaryResult, "secondary", secondaryResult);
		Map<String, String> filteredDifferences = filterDifferences(rawDifferences, noise);

		return Differences.builder()
			.rawDifferences(rawDifferences)
			.noise(noise)
			.filteredDifferences(filteredDifferences)
			.build();
	}

	private static Differences getRequestDifferences(Observation observation) {
		Map<String, Object> primaryRequest = convertToFlatMap(
				objectMapper.convertValue(observation.getPrimaryRequest(), new TypeReference<>() {
				}));
		Map<String, Object> secondaryRequest = convertToFlatMap(
				objectMapper.convertValue(observation.getSecondaryRequest(), new TypeReference<>() {
				}));
		Map<String, Object> candidateRequest = convertToFlatMap(
				objectMapper.convertValue(observation.getCandidateRequest(), new TypeReference<>() {
				}));

		Map<String, String> rawDifferences = getDifferences("primary", primaryRequest, "candidate", candidateRequest);
		Map<String, String> noise = getDifferences("primary", primaryRequest, "secondary", secondaryRequest);
		Map<String, String> filteredDifferences = filterDifferences(rawDifferences, noise);

		return Differences.builder()
			.rawDifferences(rawDifferences)
			.noise(noise)
			.filteredDifferences(filteredDifferences)
			.build();
	}

	private static Map<String, String> filterDifferences(Map<String, String> rawDifferences,
			Map<String, String> noise) {
		Map<String, String> filtered = new HashMap<>();

		for (String key : rawDifferences.keySet()) {
			if (!noise.containsKey(key)) {
				filtered.put(key, rawDifferences.get(key));
			}
		}

		return filtered;
	}

	public static Map<String, Object> convertToFlatMap(Map<String, Object> map) {
		Map<String, Object> flatMap = new HashMap<>();

		if (CollectionUtils.isEmpty(map)) {
			return Collections.emptyMap();
		}

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			processNode("", entry, flatMap);
		}

		return flatMap;
	}

	private static void processNode(String path, Map.Entry<String, Object> entry, Map<String, Object> flatMap) {
		String key = entry.getKey();
		Object value = entry.getValue();

		if (value instanceof Map) {
			Map<String, Object> valueMap = (Map<String, Object>) value;
			for (Map.Entry<String, Object> innerEntry : valueMap.entrySet()) {
				processNode(path + "/" + key, innerEntry, flatMap);
			}
		}
		else {
			flatMap.put(path + "/" + key, value);
		}
	}

	public static Map<String, String> getDifferences(String leftLabel, Map<String, Object> left, String rightLabel,
			Map<String, Object> right) {
		Map<String, String> differences = new HashMap<>();

		// Loop over keys on left
		loopOverKeys(leftLabel, left, rightLabel, right, differences);
		// Loop over keys on left
		loopOverKeys(rightLabel, right, leftLabel, left, differences);

		return differences;
	}

	private static void loopOverKeys(String leftLabel, Map<String, Object> left, String rightLabel,
			Map<String, Object> right, Map<String, String> differences) {
		for (Map.Entry<String, Object> entry : left.entrySet()) {
			if (right.containsKey(entry.getKey())) {
				if (Objects.isNull(entry.getValue())) {
					if (!Objects.isNull(right.get(entry.getKey()))) {
						differences.putIfAbsent(entry.getKey(),
								generateDifferenceString(leftLabel, "null", rightLabel, right.get(entry.getKey())));
					}
					continue;
				}
				if (!entry.getValue().equals(right.get(entry.getKey()))) {
					differences.putIfAbsent(entry.getKey(), generateDifferenceString(leftLabel, entry.getValue(),
							rightLabel, right.get(entry.getKey())));
				}
			}
			else {
				differences.putIfAbsent(entry.getKey(),
						generateDifferenceString(leftLabel, entry.getValue(), rightLabel, "null"));
			}
		}
	}

	private String generateDifferenceString(String leftLabel, Object leftValue, String rightLabel, Object rightValue) {
		try {
			var leftObjectJson = objectMapper.writeValueAsString(leftValue);
			var rightObjectJson = objectMapper.writeValueAsString(rightValue);

			return leftLabel + ": " + leftObjectJson + " --> " + rightLabel + ": " + rightObjectJson;

		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

	}

}
