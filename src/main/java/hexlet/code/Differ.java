package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.formatter.JsonFormatter;
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
    json,
    plain;

    public static FormatTypes byString(final String value) {
        for (var item : FormatTypes.values()) {
            if (item.toString().equals(value)) {
                return item;
            }
        }
        return FormatTypes.stylish;
    }
}

public final class Differ {


    private final Formatter formatter;

    public Differ(final FormatTypes formatType) {
        this.formatter = switch (formatType) {
            case stylish -> new StylishFormatter();
            case plain -> new PlainFormatter();
            case json -> new JsonFormatter();
        };
    }


    public static String generate(final String file1, final String file2)
        throws IOException {
        return Differ.generate(file1, file2, FormatTypes.stylish);
    }

    public static String generate(final String file1, final String file2,
                                  final String formatType)
        throws IOException {
        return Differ.generate(file1, file2, FormatTypes.byString(formatType));
    }

    public static String generate(final String file1,
                                  final String file2,
                                  final FormatTypes formatType)
        throws IOException {
        var differ = new Differ(formatType);
        var firstJson = new File(file1);
        var secondJson = new File(file2);

        return differ.getDiff(firstJson, secondJson);
    }

    public String getDiff(final File file1,
                          final File file2)
        throws IOException {
        var firstJson = Parser.parse(file1);
        var secondJson = Parser.parse(file2);
        var diff = collectDiff(firstJson, secondJson);

        return format(diff);
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
