package com.degressly.proxy.comparator.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class DiffEngine {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static <T> List<String> getNonDeterministicDifferences(T primaryObject, T secondaryObject,
			T candidateObject) {
		JsonNode primaryMap = objectMapper.convertValue(primaryObject, JsonNode.class);
		JsonNode secondaryMap = objectMapper.convertValue(secondaryObject, JsonNode.class);
		JsonNode candidateMap = objectMapper.convertValue(candidateObject, JsonNode.class);

		return findDeterministicDifferences(primaryMap, candidateMap, secondaryMap);
	}

	public static List<String> findDeterministicDifferences(JsonNode primary, JsonNode candidate, JsonNode secondary) {
		List<String> differences = new ArrayList<>();
		findDifferences(primary, candidate, secondary, "", differences);
		return differences;
	}

	private static void findDifferences(JsonNode primary, JsonNode candidate, JsonNode secondary, String path,
			List<String> differences) {

		if (primary.isValueNode()) {
			if (primary.asText().equals(secondary.asText())) {
				if (!primary.asText().equals(candidate.asText())) {
					differences.add(path + ": " + primary + " -> " + candidate);
				}
			}
		}

		Iterator<String> fieldNames = primary.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			if ("body".equals(fieldName)) {
				System.out.println("here");
			}
			JsonNode primaryValue = primary.get(fieldName);
			JsonNode candidateValue = candidate.get(fieldName);
			JsonNode secondaryValue = secondary != null ? secondary.get(fieldName) : null;

			if (primaryValue.isValueNode()) {
				if (primaryValue.asText().equals(secondaryValue.asText())) {
					if (!primaryValue.asText().equals(candidateValue.asText())) {
						differences.add(path + "/" + fieldName + ": " + primaryValue + " -> " + candidateValue);
					}
				}
			}
			else if (primaryValue.isObject()) {
				findDifferences(primaryValue, candidateValue, secondaryValue, path + "/" + fieldName, differences);
			}
			else if (primaryValue.isArray()) {
				for (int i = 0; i < primaryValue.size(); i++) {
					findDifferences(primaryValue.get(i), candidateValue.get(i), secondaryValue.get(i), path + "/" + i,
							differences);
				}

			}
		}
	}

}
