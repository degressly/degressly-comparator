package com.degressly.proxy.comparator.mongo;

import com.degressly.proxy.comparator.dto.ResponsesDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("traceDocument")
public class TraceDocument {

	@Id
	private String traceId;

	private ResponsesDto responses;

	private List<String> responseDiffs;

}
