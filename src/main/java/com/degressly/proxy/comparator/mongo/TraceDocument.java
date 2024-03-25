package com.degressly.proxy.comparator.mongo;

import com.degressly.proxy.comparator.dto.Observation;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.Map;

@Data
@Document("traceDocument")
public class TraceDocument {

	@Id
	private String traceId;

	Map<String, Observation> responsesMap = Collections.emptyMap();

	Map<String, Diffs> diffMap = Collections.emptyMap();

}
