package com.degressly.proxy.comparator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Observation {

	String traceId;

	String requestUrl;

	String observationType;

	DownstreamResult primaryResult;

	DownstreamResult secondaryResult;

	DownstreamResult candidateResult;

	DownstreamRequest primaryRequest;

	DownstreamRequest secondaryRequest;

	DownstreamRequest candidateRequest;

}
