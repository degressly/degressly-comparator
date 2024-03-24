package com.degressly.proxy.comparator.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diffs {

	private List<String> responseDiffs = Collections.emptyList();

	private List<String> downstreamDiffs = Collections.emptyList();

}
