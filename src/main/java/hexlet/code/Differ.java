package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.formatter.PlainFormatter;
import hexlet.code.formatter.StylishFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

enum FormatTypes {
    stylish,
    plain;

    @Override
    public String toString() {
        return this.name();
    }
}

public class Differ {


    private final Formatter formatter;

    public Differ(final FormatTypes formatType) {
        this.formatter = switch (formatType) {
            case stylish -> new StylishFormatter();
            case plain -> new PlainFormatter();
        };
    }


    public static String generate(final File file1, final File file2)
        throws IOException {
        return Differ.generate(file1, file2, FormatTypes.stylish);
    }

    public static String generate(final File file1,
                                  final File file2,
                                  final FormatTypes formatType)
        throws IOException {
        var differ = new Differ(formatType);
        var firstJson = Parser.parse(file1);
        var secondJson = Parser.parse(file2);
        var diff = differ.collectDiff(firstJson, secondJson);

        return differ.format(diff);
    }

    private String format(final List<DiffEntry> diff) {
        return formatter.format(diff);
    }

    private List<DiffEntry> collectDiff(
        final Map<String, JsonNode> firstJson,
        final Map<String, JsonNode> secondJson
    ) {
        List<DiffEntry> result = new ArrayList<>();
        for (var entry : firstJson.entrySet()) {
            var field = secondJson.get(entry.getKey());
            if (field == null) {
                result.add(
                    DiffEntry.remove(entry.getKey(), entry.getValue())
                );
                continue;
            }
            if (entry.getValue().equals(field)) {
                result.add(
                    DiffEntry.same(entry.getKey(), entry.getValue())
                );
            } else {
                result.add(
                    DiffEntry.update(entry.getKey(), entry.getValue(), field)
                );
            }
        }
        for (var entry : secondJson.entrySet()) {
            if (firstJson.get(entry.getKey()) == null) {
                result.add(
                    DiffEntry.add(entry.getKey(), entry.getValue())
                );
            }
        }

        result.sort(Comparator.comparing(DiffEntry::key));
        return result;
    }
}
