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
		TraceDocument document = traceDocumentRepository.findByTraceId(traceId);

		if (Objects.isNull(document)) {
			document = new TraceDocument();
			document.setTraceId(traceId);
		}

		Diffs diffs = document.getDiffMap().get(requestUrl);
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

		document.setDiffMap(Collections.singletonMap(requestUrl, diffs));
		document.setResponsesMap(responsesDtoMap);

		traceDocumentRepository.save(document);

	}

}
