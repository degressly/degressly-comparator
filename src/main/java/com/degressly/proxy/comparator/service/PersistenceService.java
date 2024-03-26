package com.degressly.proxy.comparator.service;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.engine.Differences;

import java.util.List;

public interface PersistenceService {

	void save(String traceId, String requestUrl, Observation observation, Differences responseDiffs,
			Differences downstreamDiffs);

}
