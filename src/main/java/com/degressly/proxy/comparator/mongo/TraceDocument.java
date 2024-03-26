package com.degressly.proxy.comparator.mongo;

import com.degressly.proxy.comparator.dto.Observation;
import com.degressly.proxy.comparator.engine.Differences;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Document("traceDocument")
public class TraceDocument {

	@Id
	private String traceId;

	Map<String, List<Observation>> observationMap = Collections.emptyMap();

	Map<String, Differences> responseDiffs = Collections.emptyMap();

	Map<String, Differences> downstreamDiffs = Collections.emptyMap();

}
