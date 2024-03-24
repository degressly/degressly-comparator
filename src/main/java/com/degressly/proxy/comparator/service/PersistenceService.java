package com.degressly.proxy.comparator.service;

import com.degressly.proxy.comparator.dto.ResponsesDto;
import com.degressly.proxy.comparator.mongo.TraceDocument;

import java.util.List;

public interface PersistenceService {

	void save(String traceId, String requestUrl, ResponsesDto responsesDto, List<String> responseDiffs,
			List<String> downstreamDiffs);

}
