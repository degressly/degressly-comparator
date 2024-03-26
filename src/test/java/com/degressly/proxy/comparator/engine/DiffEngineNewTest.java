package com.degressly.proxy.comparator.engine;

import com.degressly.proxy.comparator.dto.DownstreamRequest;
import com.degressly.proxy.comparator.dto.Observation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Map;

import static com.degressly.proxy.comparator.engine.DiffEngineNew.getDifferences;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DiffEngineNewTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void getNonDeterministicDifferences() {

		var obs1 = getObservation();
		var map1 = DiffEngineNew.convertToFlatMap(objectMapper.convertValue(obs1, new TypeReference<>() {
		}));

		System.out.println(DiffEngineNew.getNonDeterministicDifferences(obs1));
	}

	private Observation getObservation() {
		var observation = new Observation();
		observation.setPrimaryRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("{\"c\":\"d\"}")
			.params(Collections.singletonMap("e", Collections.singletonList("f")))
			.build());
		observation.setSecondaryRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("{\"c\":\"d\"}")
			.params(Map.of("e", Collections.singletonList("f"), "g", Collections.singletonList("x")))
			.build());
		observation.setCandidateRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("{\"c\":\"d\"}")
			.params(Map.of("e", Collections.singletonList("f"), "g", Collections.singletonList("h")))
			.build());
		return observation;
	}

}