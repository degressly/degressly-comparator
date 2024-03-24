package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.mongo.TraceDocument;
import com.degressly.proxy.comparator.mongo.TraceDocumentRepository;
import com.degressly.proxy.comparator.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@ConditionalOnProperty("spring.data.mongodb.uri")
public class MongoPersistenceServiceImpl implements PersistenceService {

	@Autowired
	TraceDocumentRepository traceDocumentRepository;

	@Override
	public void save(String traceId, List<String> responseDiffs, List<String> downstreamDiffs) {
		TraceDocument document = traceDocumentRepository.findByTraceId(traceId);

		if (Objects.isNull(document)) {
			document = new TraceDocument();
			document.setTraceId(traceId);
		}

		List<String> existingResponseDiffs = document.getResponseDiffs();
		responseDiffs.addAll(existingResponseDiffs);
		document.setResponseDiffs(responseDiffs);
		List<String> existingDownstreamDiffs = document.getDownstreamDiffs();
		downstreamDiffs.addAll(existingDownstreamDiffs);
		document.setDownstreamDiffs(downstreamDiffs);

		traceDocumentRepository.save(document);

	}

}
