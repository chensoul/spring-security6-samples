package com.chensoul.security.spring;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Valerii Sosliuk on 5/12/2017.
 */
@Slf4j
public class JacksonUtil {

    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .build();
    public static final ObjectMapper PRETTY_SORTED_JSON_MAPPER = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .build();
    public static final ObjectMapper IGNORE_UNKNOWN_PROPERTIES_JSON_MAPPER = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    public static ObjectMapper ALLOW_UNQUOTED_FIELD_NAMES_MAPPER = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), false)
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .build();

    public static ObjectMapper getObjectMapperWithJavaTimeModule() {
        return JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return fromValue != null ? OBJECT_MAPPER.convertValue(fromValue, toValueType) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The given object value cannot be converted to " + toValueType + ": " + fromValue, e);
        }
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            return fromValue != null ? OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The given object value cannot be converted to " + toValueTypeRef + ": " + fromValue, e);
        }
    }

    public static <T> T fromInputStream(InputStream inputStream, Class<T> clazz) {
        try {
            return inputStream != null ? OBJECT_MAPPER.readValue(inputStream, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid request payload", e);
        }
    }

    public static <T> T fromReader(Reader reader, Class<T> clazz) {
        try {
            return reader != null ? OBJECT_MAPPER.readValue(reader, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid request payload", e);
        }
    }

    public static <T> T fromString(String string, Class<T> clazz) {
        try {
            return string != null ? OBJECT_MAPPER.readValue(string, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromString(String string, TypeReference<T> valueTypeRef) {
        try {
            return string != null ? OBJECT_MAPPER.readValue(string, valueTypeRef) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromString(String string, JavaType javaType) {
        try {
            return string != null ? OBJECT_MAPPER.readValue(string, javaType) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given String value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromString(String string, Class<T> clazz, boolean ignoreUnknownFields) {
        try {
            return string != null ? IGNORE_UNKNOWN_PROPERTIES_JSON_MAPPER.readValue(string, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + string, e);
        }
    }

    public static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
        try {
            return bytes != null ? OBJECT_MAPPER.readValue(bytes, clazz) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given byte[] value cannot be transformed to Json object:" + Arrays.toString(bytes), e);
        }
    }

    public static <T> T fromBytes(byte[] bytes, TypeReference<T> valueTypeRef) {
        try {
            return bytes != null ? OBJECT_MAPPER.readValue(bytes, valueTypeRef) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
        }
    }

    public static JsonNode fromBytes(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readTree(bytes);
        } catch (IOException e) {
            throw new IllegalArgumentException("The given byte[] value cannot be transformed to Json object: " + Arrays.toString(bytes), e);
        }
    }

    public static String toString(Object value) {
        try {
            return value != null ? OBJECT_MAPPER.writeValueAsString(value) : null;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("The given Json object value cannot be transformed to a String: " + value, e);
        }
    }

    public static String toPrettyString(Object o) {
        try {
            return PRETTY_SORTED_JSON_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPlainText(String data) {
        if (data == null) {
            return null;
        }
        if (data.startsWith("\"") && data.endsWith("\"") && data.length() >= 2) {
            final String dataBefore = data;
            try {
                data = JacksonUtil.fromString(data, String.class);
            } catch (Exception ignored) {
            }
            log.trace("Trimming double quotes. Before trim: [{}], after trim: [{}]", dataBefore, data);
        }
        return data;
    }

    public static <T> byte[] toBytes(T value) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("The given Json object value cannot be transformed to a String: " + value, e);
        }
    }

    public static <T> void writeValue(Writer writer, T value) {
        try {
            OBJECT_MAPPER.writeValue(writer, value);
        } catch (IOException e) {
            throw new IllegalArgumentException("The given writer value: "
                    + writer + "cannot be wrote", e);
        }
    }

    public static <T> T treeToValue(JsonNode node, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(node, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't convert value: " + node.toString(), e);
        }
    }

    public static JsonNode readTree(Object value) {
        return readTree(toString(value), OBJECT_MAPPER);
    }

    public static JsonNode readTree(String value) {
        return readTree(value, OBJECT_MAPPER);
    }

    public static JsonNode readTree(Path file) {
        try {
            return OBJECT_MAPPER.readTree(Files.readAllBytes(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static JsonNode readTree(File value) {
        try {
            return value != null ? OBJECT_MAPPER.readTree(value) : null;
        } catch (IOException e) {
            throw new IllegalArgumentException("The given File object value: "
                    + value + " cannot be transformed to a JsonNode", e);
        }
    }

    public static JsonNode readTree(String value, ObjectMapper mapper) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return mapper.readTree(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> JsonNode valueToTree(T value) {
        return OBJECT_MAPPER.valueToTree(value);
    }

    public static JsonNode getSafely(JsonNode node, String... path) {
        if (node == null) {
            return null;
        }
        for (String p : path) {
            if (!node.has(p)) {
                return null;
            } else {
                node = node.get(p);
            }
        }
        return node;
    }

    public static <T> T readValue(String file, CollectionType clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static <T> T readValue(File file, TypeReference<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static <T> T readValue(File file, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read file: " + file, e);
        }
    }

    public static ObjectNode newObjectNode() {
        return newObjectNode(OBJECT_MAPPER);
    }

    public static ObjectNode newObjectNode(ObjectMapper mapper) {
        return mapper.createObjectNode();
    }

    public static ArrayNode newArrayNode() {
        return newArrayNode(OBJECT_MAPPER);
    }

    public static ArrayNode newArrayNode(ObjectMapper mapper) {
        return mapper.createArrayNode();
    }

    public static <T> T clone(T value) {
        @SuppressWarnings("unchecked")
        Class<T> valueClass = (Class<T>) value.getClass();
        return fromString(toString(value), valueClass);
    }

    public static ObjectNode asObject(JsonNode node) {
        return node != null && node.isObject() ? ((ObjectNode) node) : newObjectNode();
    }

    public static JavaType constructCollectionType(Class collectionClass, Class<?> elementClass) {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public static Map<String, String> toFlatMap(JsonNode node) {
        HashMap<String, String> map = new HashMap<>();
        toFlatMap(node, "", map);
        return map;
    }

    private static void toFlatMap(JsonNode node, String currentPath, Map<String, String> map) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            currentPath = currentPath.isEmpty() ? "" : currentPath + ".";
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                toFlatMap(entry.getValue(), currentPath + entry.getKey(), map);
            }
        } else if (node.isValueNode()) {
            map.put(currentPath, node.asText());
        }
    }
}
