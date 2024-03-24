package com.degressly.proxy.comparator.mongo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty("spring.data.mongodb.uri")
public interface TraceDocumentRepository extends MongoRepository<TraceDocument, String> {

	TraceDocument findByTraceId(String traceId);

}
