package com.degressly.proxy.comparator.dto;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Data
public class DownstreamResult {

	private Map<String, Object> httpResponse;

	private Exception exception;

}
