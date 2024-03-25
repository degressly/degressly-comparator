package com.degressly.proxy.comparator.service;

import com.degressly.proxy.comparator.dto.Observation;

import java.util.List;

public interface PersistenceService {

	void save(String traceId, String requestUrl, Observation observation, List<String> responseDiffs,
			List<String> downstreamDiffs);

}
