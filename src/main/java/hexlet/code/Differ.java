package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.formatter.StylishFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Differ {
    private final ObjectMapper mapper = new ObjectMapper();
    public static final int INDENT_SIZE = 2;

    public static String generate(File file1, File file2) throws IOException {
        var differ = new Differ();
        var firstJson = differ.jsonToMap(file1);
        var secondJson = differ.jsonToMap(file2);
        var diff = differ.collectDiff(firstJson, secondJson);

        return new StylishFormatter(INDENT_SIZE).format(diff);
    }

    private List<DiffEntry> collectDiff(Map<String, JsonNode> firstJson, Map<String, JsonNode> secondJson) {
        List<DiffEntry> result = new ArrayList<>();
        for (var entry : firstJson.entrySet()) {
            var field = secondJson.get(entry.getKey());
            if (field == null) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(), Operation.remove));
                continue;
            }
            if (entry.getValue().equals(field)) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(), Operation.equal));
            }
            if (!entry.getValue().equals(field)) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(), Operation.remove));
                result.add(new DiffEntry(entry.getKey(), field, Operation.add));
            }
        }
        for (var entry : secondJson.entrySet()) {
            if (firstJson.get(entry.getKey()) == null) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(), Operation.add));
            }
        }

        return result;
    }

    private Map<String, JsonNode> jsonToMap(File file) throws IOException {
        Map<String, JsonNode> json = sortMap(mapper.readValue(file, new TypeReference<>() {
        }));

        return sortMap(json);
    }

    private Map<String, JsonNode> sortMap(Map<String, JsonNode> map) {
        return new TreeMap<>(map);
    }
}
