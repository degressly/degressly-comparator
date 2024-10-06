package com.degressly.proxy.comparator.dto;

import com.degressly.proxy.comparator.engine.BodySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DownstreamResult {

	private Map<String, List<String>> headers;

	private String statusCode;

	@JsonSerialize(using = BodySerializer.class)
	private String body;

	private String exception;

}
