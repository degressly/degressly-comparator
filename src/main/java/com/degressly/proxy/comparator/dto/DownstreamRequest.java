package com.degressly.proxy.comparator.dto;

import com.degressly.proxy.comparator.engine.BodySerializer;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownstreamRequest {

	Map<String, List<String>> headers;

	Map<String, List<String>> params;

	@JsonSerialize(using = BodySerializer.class)
	String body;

}
