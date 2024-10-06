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

@RunWith(MockitoJUnitRunner.Silent.class)
public class DiffEngineTest {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void getNonDeterministicDifferences() {

		var obs1 = getObservation();
		Map<String, Object> map1 = DiffEngine.convertToFlatMap(objectMapper.convertValue(obs1, new TypeReference<>() {
		}));

		System.out.println(map1);
		System.out.println(DiffEngine.getNonDeterministicDifferences(obs1));

		System.out.println(DiffEngine.getNonDeterministicDifferences(getObservationXml()));

	}

	private Observation getObservation() {
		var observation = new Observation();
		observation.setPrimaryRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("{\"c\":\"d\",\"x\":{\"y\":\"z\"}}")
			.params(Collections.singletonMap("e", Collections.singletonList("f")))
			.build());
		observation.setSecondaryRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("{\"c\":\"d\",\"x\":{\"y\":\"z\"}}")
			.params(Map.of("e", Collections.singletonList("f"), "g", Collections.singletonList("x")))
			.build());
		observation.setCandidateRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("{\"c\":\"d\",\"x\":{\"y\":\"z2\"}}")
			.params(Map.of("e", Collections.singletonList("f"), "g", Collections.singletonList("h")))
			.build());
		return observation;
	}

	private Observation getObservationXml() {
		var observation = new Observation();
		observation.setPrimaryRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><book category=\"web\" cover=\"paperback\"><title lang=\"en\">Learning XML</title><author>Erik T. Ray</author><year>2003</year><price>39.95</price></book></root>\n")
			.params(Collections.singletonMap("e", Collections.singletonList("f")))
			.build());
		observation.setSecondaryRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><book category=\"web\" cover=\"paperback\"><title lang=\"en\">Learning XML</title><author>Erik T. Ray</author><year>2003</year><price>39.95</price></book></root>\n")
			.params(Map.of("e", Collections.singletonList("f"), "g", Collections.singletonList("x")))
			.build());
		observation.setCandidateRequest(DownstreamRequest.builder()
			.headers(Collections.singletonMap("a", Collections.singletonList("b")))
			.body("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><book category=\"web\" cover=\"paperback\"><title lang=\"en\">Learning XML</title><author>Erik T. Ray</author><year>2003</year><price>39.94</price></book></root>\n")
			.params(Map.of("e", Collections.singletonList("f"), "g", Collections.singletonList("h")))
			.build());
		return observation;
	}

}