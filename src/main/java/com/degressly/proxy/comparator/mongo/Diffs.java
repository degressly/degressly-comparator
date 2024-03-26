package com.degressly.proxy.comparator.mongo;

import com.degressly.proxy.comparator.engine.Differences;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diffs {

	private Map<String, Differences> responseDiffs = new HashMap<>();

	private Map<String, Differences> downstreamDiffs = new HashMap<>();

}
