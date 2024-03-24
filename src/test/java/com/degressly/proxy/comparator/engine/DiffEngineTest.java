package com.degressly.proxy.comparator.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static com.degressly.proxy.comparator.engine.DiffEngine.findDeterministicDifferences;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DiffEngineTest {

	@Test
	public void testFindDeterministicDifferences() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		String primaryJsonString = "{\"name\": \"John\", \"age\": 30, \"test\":[{\"a\": true},2,3], \"address\": {\"city\": \"New York\", \"zip\": 10001}}";
		String candidateJsonString = "{\"name\": \"John\", \"age\": 31, \"test\":[{\"a\": false},2,3], \"address\": {\"city\": \"Los Angeles\", \"zip\": 90001}}";
		String secondaryJsonString = "{\"name\": \"John\", \"age\": 30,  \"test\":[{\"a\": true},2,3],\"address\": {\"city\": \"New York\", \"zip\": 10001}}";

		JsonNode primaryJson = objectMapper.readTree(primaryJsonString);
		JsonNode candidateJson = objectMapper.readTree(candidateJsonString);
		JsonNode secondaryJson = objectMapper.readTree(secondaryJsonString);

		List<String> differences = findDeterministicDifferences(primaryJson, candidateJson, secondaryJson);
		Assert.assertEquals(3, differences.size());
		Assert.assertArrayEquals(
				new String[] { "/age: 30 -> 31", "/test: [{\"a\":true},2,3] -> [{\"a\":false},2,3]",
						"/address: {\"city\":\"New York\",\"zip\":10001} -> {\"city\":\"Los Angeles\",\"zip\":90001}" },
				differences.toArray());
		differences.forEach(System.out::println);
	}

}