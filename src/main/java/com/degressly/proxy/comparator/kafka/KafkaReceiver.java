package com.degressly.proxy.comparator.kafka;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.service.DiffService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {

	private static final Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

	@Autowired
	DiffService diffService;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@KafkaListener(topics = "${diff.publisher.topic-name}", groupId = "${diff.publisher.group-id:comparator_default}")
	public void listen(String message) throws JsonProcessingException {
		try {
			logger.info("Message receiver: {}", message);
			var responses = objectMapper.readValue(message, Observation.class);
			diffService.process(responses);
		}
		catch (Exception e) {
			logger.error("Error when processing observation", e);
		}
	}

}
