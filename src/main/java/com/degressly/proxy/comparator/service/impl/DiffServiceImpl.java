package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.dto.ResponsesDto;
import com.degressly.proxy.comparator.service.DiffService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DiffServiceImpl implements DiffService {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void process(ResponsesDto responsesDto) {
		try {
			Map<String, Object> primaryMap = objectMapper
				.readValue(objectMapper.writeValueAsString(responsesDto.getPrimaryResult()), new TypeReference<>() {
				});
			Map<String, Object> secondaryMap = objectMapper
				.readValue(objectMapper.writeValueAsString(responsesDto.getSecondaryResult()), new TypeReference<>() {
				});
			Map<String, Object> candidateMap = objectMapper
				.readValue(objectMapper.writeValueAsString(responsesDto.getCandidateResult()), new TypeReference<>() {
				});

			MapDifference<String, Object> primaryVsSecondary = Maps.difference(primaryMap, secondaryMap);
			MapDifference<String, Object> primaryVsCandidate = Maps.difference(primaryMap, candidateMap);

			System.out.println(primaryVsSecondary);
			System.out.println(primaryVsCandidate);

		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
