package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.service.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogPersistenceServiceImpl implements PersistenceService {

	Logger logger = LoggerFactory.getLogger(LogPersistenceServiceImpl.class);

	@Override
	public void save(String traceId, List<String> responseDiffs, List<String> downstreamDiffs) {
		logger.info("traceId {} \n responseDiff: {} \n downstreamDiff: {} ", traceId, responseDiffs, downstreamDiffs);
	}

}
