package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.engine.Differences;
import com.degressly.proxy.comparator.mongo.Diffs;
import com.degressly.proxy.comparator.mongo.TraceDocument;
import com.degressly.proxy.comparator.mongo.TraceDocumentRepository;
import com.degressly.proxy.comparator.service.PersistenceService;
import org.apache.commons.lang3.builder.Diff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@ConditionalOnProperty("spring.data.mongodb.uri")
public class MongoPersistenceServiceImpl implements PersistenceService {

	@Autowired
	TraceDocumentRepository traceDocumentRepository;

	@Override
	public void save(String traceId, String requestUrl, Observation observation, Differences responseDiffs,
			Differences downstreamDiffs) {

		// Replacing . with _ in map key due to mongo limitations
		requestUrl = requestUrl.replace('.', '_');

		TraceDocument document = traceDocumentRepository.findByTraceId(traceId);

		if (document == null) {
			document = new TraceDocument();
			document.setTraceId(traceId);
		}

		updateObservationMap(requestUrl, observation, document);

		if ("RESPONSE".equals(observation.getObservationType())) {
			updateResponseDiffMap(requestUrl, responseDiffs, document);
		}

		if ("REQUEST".equals(observation.getObservationType())) {
			updateDownstreamDiffMap(requestUrl, downstreamDiffs, document);
		}

		traceDocumentRepository.save(document);

	}

	private static void updateDownstreamDiffMap(String requestUrl, Differences responseDiffs, TraceDocument document) {
		Map<String, Differences> existingDownstreamDifferencesMap = document.getDownstreamDiffs();
		Map<String, Differences> newDownstreamDifferencesMap = new HashMap<>();
		newDownstreamDifferencesMap.put(requestUrl, responseDiffs);
		newDownstreamDifferencesMap.putAll(existingDownstreamDifferencesMap);
		document.setDownstreamDiffs(newDownstreamDifferencesMap);
	}

	private static void updateResponseDiffMap(String requestUrl, Differences responseDiffs, TraceDocument document) {
		Map<String, Differences> existingResponseDifferencesMap = document.getResponseDiffs();
		Map<String, Differences> newResponseDifferencesMap = new HashMap<>();
		newResponseDifferencesMap.put(requestUrl, responseDiffs);
		newResponseDifferencesMap.putAll(existingResponseDifferencesMap);
		document.setResponseDiffs(newResponseDifferencesMap);
	}

	private static void updateObservationMap(String requestUrl, Observation observation, TraceDocument document) {
		Map<String, List<Observation>> existingObservationsMap = document.getObservationMap();
		Map<String, List<Observation>> newObservationsMap = new HashMap<>();

		List<Observation> observationsList = existingObservationsMap.get(requestUrl);
		List<Observation> newObservationsList = new ArrayList<>(Arrays.asList(observation));
		if (!CollectionUtils.isEmpty(observationsList)) {
			newObservationsList.addAll(observationsList);
		}

		newObservationsMap.put(requestUrl, newObservationsList);
		newObservationsMap.putAll(existingObservationsMap);
		document.setObservationMap(newObservationsMap);
	}

}
