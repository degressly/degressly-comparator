package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.engine.DiffEngine;
import com.degressly.proxy.comparator.service.DiffService;
import com.degressly.proxy.comparator.service.PersistenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DiffServiceImpl implements DiffService {

	@Autowired
	List<PersistenceService> persistenceServices;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void process(Observation observation) {

		var diffs = DiffEngine.getNonDeterministicDifferences(observation);
		List<String> requestDiffs = diffs.getFirst();
		List<String> responseDiffs = diffs.getSecond();

		persistenceServices.forEach((persistenceService -> persistenceService.save(observation.getTraceId(),
				observation.getRequestUrl(), observation, responseDiffs, requestDiffs)));

	}

}
