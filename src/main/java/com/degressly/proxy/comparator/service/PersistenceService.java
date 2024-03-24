package com.degressly.proxy.comparator.service;

import com.degressly.proxy.comparator.mongo.TraceDocument;

import java.util.List;

public interface PersistenceService {

	public void save(String traceId, List<String> responseDiffs, List<String> downstreamDiffs);

}
