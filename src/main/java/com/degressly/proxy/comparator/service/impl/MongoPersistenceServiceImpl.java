package com.degressly.proxy.comparator.service.impl;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.engine.Differences;
import com.degressly.proxy.comparator.engine.Utils;
import com.degressly.proxy.comparator.mongo.TraceDocument;
import com.degressly.proxy.comparator.mongo.TraceDocumentRepository;
import com.degressly.proxy.comparator.service.PersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@ConditionalOnProperty("spring.data.mongodb.uri")
public class MongoPersistenceServiceImpl implements PersistenceService {

	@Autowired
	TraceDocumentRepository traceDocumentRepository;

	@Override
	public void save(String traceId, String requestUrl, Observation observation, Differences responseDiffs,
			Differences downstreamDiffs) {

		synchronized (this) {
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

			if ("REQUEST".equals(observation.getObservationType())
					|| "KAFKA".equals(observation.getObservationType())) {
				updateDownstreamDiffMap(requestUrl, downstreamDiffs, document);
			}

			saveDocument(document);
		}

	}

	@Retryable(maxAttemptsExpression = "${mongo.retry.max.attempts:8}",
			backoff = @Backoff(delayExpression = "${mongo.retry.delay:1000}", multiplier = 2))
	private void saveDocument(TraceDocument document) {
		log.info("Saving: {}", document);
		traceDocumentRepository.save(document);
	}

	private static void updateDownstreamDiffMap(String requestUrl, Differences responseDiffs, TraceDocument document) {
		Map<String, Differences> existingDownstreamDifferencesMap = document.getDownstreamDiffs();
		Map<String, Differences> newDownstreamDifferencesMap = new HashMap<>();
		newDownstreamDifferencesMap.putAll(existingDownstreamDifferencesMap);
		newDownstreamDifferencesMap.put(requestUrl, responseDiffs);
		document.setDownstreamDiffs(newDownstreamDifferencesMap);
	}

	private static void updateResponseDiffMap(String requestUrl, Differences responseDiffs, TraceDocument document) {
		Map<String, Differences> existingResponseDifferencesMap = document.getResponseDiffs();
		Map<String, Differences> newResponseDifferencesMap = new HashMap<>();
		newResponseDifferencesMap.putAll(existingResponseDifferencesMap);
		newResponseDifferencesMap.put(requestUrl, responseDiffs);
		document.setResponseDiffs(newResponseDifferencesMap);
	}

	private static void updateObservationMap(String requestUrl, Observation observation, TraceDocument document) {
		Map<String, List<Observation>> existingObservationsMap = document.getObservationMap();

		List<Observation> observationsList = existingObservationsMap
			.get(observation.getObservationType() + "_" + requestUrl);
		List<Observation> newObservationsList = new ArrayList<>(Arrays.asList(observation));
		if (!CollectionUtils.isEmpty(observationsList)) {
			newObservationsList = combineObservationLists(observationsList, newObservationsList);
		}

		Map<String, List<Observation>> newObservationsMap = new HashMap<>(existingObservationsMap);
		newObservationsMap.put(observation.getObservationType() + "_" + requestUrl, newObservationsList);
		document.setObservationMap(newObservationsMap);
	}

	private static List<Observation> combineObservationLists(List<Observation> observationsList,
			List<Observation> newObservationsList) {
		List<Observation> smallerList = observationsList.size() < newObservationsList.size() ? observationsList
				: newObservationsList;
		List<Observation> largerList = observationsList.size() >= newObservationsList.size() ? observationsList
				: newObservationsList;

		IntStream.range(0, smallerList.size()).forEach(i -> {
			try {
				largerList.set(i, Utils.mergeObjects(smallerList.get(i), largerList.get(i)));
			}
			catch (Exception e) {
				log.error("Error when merging Observations: ", e);
			}

		});

		return largerList;
	}

}
