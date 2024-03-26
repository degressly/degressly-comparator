package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.mongo.Diffs;
import com.degressly.proxy.comparator.mongo.TraceDocument;
import com.degressly.proxy.comparator.mongo.TraceDocumentRepository;
import com.degressly.proxy.comparator.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ConditionalOnProperty("spring.data.mongodb.uri")
public class MongoPersistenceServiceImpl implements PersistenceService {

	@Autowired
	TraceDocumentRepository traceDocumentRepository;

	@Override
	public void save(String traceId, String requestUrl, Observation observation, List<String> responseDiffs,
			List<String> downstreamDiffs) {

		// TODO: Fix possible race condition here where two observations come at the same
		// time, both will try to create a new document.
		TraceDocument document = traceDocumentRepository.findByTraceId(traceId);

		if (Objects.isNull(document)) {
			document = new TraceDocument();
			document.setTraceId(traceId);
		}

		Map<String, Diffs> currentDiffMap = document.getDiffMap();
		Diffs diffs = currentDiffMap.get(requestUrl);
		if (Objects.isNull(diffs)) {
			diffs = new Diffs();
		}

		List<String> existingResponseDiffs = diffs.getResponseDiffs();
		responseDiffs.addAll(existingResponseDiffs);
		diffs.setResponseDiffs(responseDiffs);

		List<String> existingDownstreamDiffs = diffs.getDownstreamDiffs();
		downstreamDiffs.addAll(existingDownstreamDiffs);
		diffs.setDownstreamDiffs(downstreamDiffs);

		Map<String, Observation> responsesDtoMap = new HashMap<>(document.getResponsesMap());
		responsesDtoMap.put(requestUrl, observation);

		Map<String, Diffs> newDiffs = new HashMap<>(Map.of(requestUrl, diffs));
		newDiffs.putAll(currentDiffMap);
		document.setDiffMap(newDiffs);
		document.setResponsesMap(responsesDtoMap);

		traceDocumentRepository.save(document);

	}

}
