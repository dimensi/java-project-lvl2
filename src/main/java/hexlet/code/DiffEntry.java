package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;

public record DiffEntry(String key, JsonNode value, Operation operation) {
}