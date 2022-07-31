package hexlet.code.formatter;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.DiffEntry;
import hexlet.code.Formatter;
import hexlet.code.Operation;

import java.util.List;
import java.util.stream.Collectors;

public final class PlainFormatter implements Formatter {
    private String formatValue(final JsonNode value) {
        if (value.isArray() || value.isObject()) {
            return "[complex value]";
        }
        if (value.isTextual()) {
            return String.format("'%s'", value.textValue());
        }
        return value.toString();
    }

    private String formatRemoveText(final DiffEntry entry) {
        return String.format("Property '%s' was removed", entry.key());
    }

    private String formatAddText(final DiffEntry entry) {
        var formattedValue = formatValue(entry.value());
        return String.format("Property '%s' was added with value: %s",
            entry.key(),
            formattedValue);
    }

    private String formatUpdateText(final DiffEntry entry) {
        var formattedValue = formatValue(entry.value());
        var formattedNewValue = formatValue(entry.newValue());

        return String.format("Property '%s' was updated. From %s to %s",
            entry.key(), formattedValue, formattedNewValue);
    }

    @Override
    public String format(final List<DiffEntry> json) {
        return json.stream()
            .filter(diffEntry -> !diffEntry.operation().equals(Operation.same))
            .map(diffEntry -> switch (diffEntry.operation()) {
                case update -> formatUpdateText(diffEntry);
                case add -> formatAddText(diffEntry);
                case remove -> formatRemoveText(diffEntry);
                default -> "";
            })
            .collect(Collectors.joining("\n"));
    }
}
