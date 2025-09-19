package com.kafka.kafka_stream.transformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import jakarta.annotation.PostConstruct;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventTransformer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Schema> schemaCache = new HashMap<>();


    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    private CachedSchemaRegistryClient client;

    @PostConstruct
    public void init() {
        this.client = new CachedSchemaRegistryClient(schemaRegistryUrl, 100);
    }

    /**
     * Convert JSON string to Avro GenericRecord using schema from registry.
     *
     * @param subject Schema Registry subject (e.g., "user-events-value")
     * @param version Schema version (null = latest)
     * @param json    JSON input string
     * @return Avro GenericRecord
     */
    public GenericRecord toGenericRecord(String subject, Integer version, String json) {
        try {
            Schema schema = getSchema(subject, version);
            JsonNode node = mapper.readTree(json);
            return buildRecord(node, schema);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON", e);
        } catch (Exception e) {
            throw new RuntimeException("JSON → Avro transformation failed", e);
        }
    }

    private Schema getSchema(String subject, Integer version) throws Exception {
        String key = subject + (version == null ? "-latest" : "-v" + version);
        if (!schemaCache.containsKey(key)) {
            var md = (version != null)
                    ? client.getSchemaMetadata(subject, version)
                    : client.getLatestSchemaMetadata(subject);
            schemaCache.put(key, new Schema.Parser().parse(md.getSchema()));
        }
        return schemaCache.get(key);
    }

    private GenericRecord buildRecord(JsonNode json, Schema schema) {
        GenericData.Record rec = new GenericData.Record(schema);

        for (Schema.Field f : schema.getFields()) {
            String name = f.name();
            JsonNode value = json.get(name);

            Schema fieldSchema = unwrapNullable(f.schema());

            if (value == null || value.isNull()) {
                if (isNullable(f.schema())) {
                    rec.put(name, null);
                } else {
                    throw new IllegalArgumentException("Missing non-nullable field: " + name);
                }
                continue;
            }

            rec.put(name, castValue(value, fieldSchema));
        }
        return rec;
    }

    private Object castValue(JsonNode n, Schema s) {
        return switch (s.getType()) {
            case STRING -> n.asText();
            case INT -> n.asInt();
            case LONG -> n.asLong();
            case BOOLEAN -> n.asBoolean();
            case DOUBLE, FLOAT -> n.asDouble();
            case RECORD -> buildRecord(n, s);
            case ARRAY -> {
                List<Object> list = new java.util.ArrayList<>();
                for (JsonNode el : n) {
                    list.add(castValue(el, s.getElementType()));
                }
                yield list;
            }
            case MAP -> {
                Map<String, Object> map = new HashMap<>();
                n.fields().forEachRemaining(e ->
                        map.put(e.getKey(), castValue(e.getValue(), s.getValueType())));
                yield map;
            }
            case ENUM -> new GenericData.EnumSymbol(s, n.asText());
            case UNION -> castValue(n, unwrapNullable(s));
            case BYTES, FIXED -> {
                try {
                    yield n.binaryValue();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read bytes for field", e);
                }
            }
            case NULL -> null;
        };
    }

    private boolean isNullable(Schema schema) {
        return schema.getType() == Schema.Type.UNION &&
                schema.getTypes().stream().anyMatch(s -> s.getType() == Schema.Type.NULL);
    }

    private Schema unwrapNullable(Schema schema) {
        if (schema.getType() == Schema.Type.UNION) {
            return schema.getTypes().stream()
                    .filter(s -> s.getType() != Schema.Type.NULL)
                    .findFirst().orElseThrow();
        }
        return schema;
    }
}
