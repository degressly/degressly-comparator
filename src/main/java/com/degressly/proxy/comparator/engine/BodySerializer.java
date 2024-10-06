package com.degressly.proxy.comparator.engine;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.Map;

public class BodySerializer extends JsonSerializer<String> {

	private final ObjectMapper jsonMapper = new ObjectMapper();

	private final XmlMapper xmlMapper = new XmlMapper();

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		try {
			Map<String, Object> jsonMap = jsonMapper.readValue(value, new TypeReference<>() {
			});
			gen.writeObject(jsonMap);
		}
		catch (Exception jsonE) {
			try {
				Map<String, Object> xmlMap = xmlMapper.readValue(value, Map.class);
				gen.writeObject(xmlMap);
			}
			catch (Exception xmlE) {
				gen.writeObject(value);
			}
		}
	}

}
