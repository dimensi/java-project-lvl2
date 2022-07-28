package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Differ {
    public static final int INDENT_SIZE = 2;
    public static String generate(File file1, File file2) throws IOException {
        var mapper = new ObjectMapper();
        Map<String, JsonNode> firstJson = Differ.sortMap(mapper.readValue(file1, new TypeReference<>() {
        }));
        Map<String, JsonNode> secondJson = Differ.sortMap(mapper.readValue(file2, new TypeReference<>() {
        }));
        Map<String, JsonNode> result = new LinkedHashMap<>();
        for (var entry : firstJson.entrySet()) {
            var field = secondJson.get(entry.getKey());
            if (field == null) {
                result.put(String.format("- %s", entry.getKey()), entry.getValue());
                continue;
            }
            if (entry.getValue().equals(field)) {
                result.put(String.format("  %s", entry.getKey()), entry.getValue());
            }
            if (!entry.getValue().equals(field)) {
                result.put(String.format("- %s", entry.getKey()), entry.getValue());
                result.put(String.format("+ %s", entry.getKey()), field);
            }
        }
        for (var entry : secondJson.entrySet()) {
            if (firstJson.get(entry.getKey()) == null) {
                result.put(String.format("+ %s", entry.getKey()), entry.getValue());
            }
        }

        var builder = new StringBuilder();
        builder.append("{\n");
        for (var entry : result.entrySet()) {
            builder.append(String.format("%s%s: %s%n", " ".repeat(INDENT_SIZE), entry.getKey(), entry.getValue()));
        }
        builder.append("}");

        return builder.toString();
    }

    public static Map<String, JsonNode> sortMap(Map<String, JsonNode> map) {
        return new TreeMap<>(map);
    }
}
