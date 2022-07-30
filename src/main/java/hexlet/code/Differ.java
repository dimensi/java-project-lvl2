package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.formatter.StylishFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Differ {

    public static final int INDENT_SIZE = 2;

    public static String generate(final File file1, final File file2)
        throws IOException {
        var differ = new Differ();
        var firstJson = Parser.parse(file1);
        var secondJson = Parser.parse(file2);
        var diff = differ.collectDiff(firstJson, secondJson);

        return new StylishFormatter(INDENT_SIZE).format(diff);
    }

    private List<DiffEntry> collectDiff(
        final Map<String, JsonNode> firstJson,
        final Map<String, JsonNode> secondJson
    ) {
        List<DiffEntry> result = new ArrayList<>();
        for (var entry : firstJson.entrySet()) {
            var field = secondJson.get(entry.getKey());
            if (field == null) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(),
                    Operation.remove));
                continue;
            }
            if (entry.getValue().equals(field)) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(),
                    Operation.equal));
            }
            if (!entry.getValue().equals(field)) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(),
                    Operation.remove));
                result.add(new DiffEntry(entry.getKey(), field, Operation.add));
            }
        }
        for (var entry : secondJson.entrySet()) {
            if (firstJson.get(entry.getKey()) == null) {
                result.add(new DiffEntry(entry.getKey(), entry.getValue(),
                    Operation.add));
            }
        }

        return result;
    }
}
