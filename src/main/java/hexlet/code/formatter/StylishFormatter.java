package hexlet.code.formatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hexlet.code.DiffEntry;
import hexlet.code.Formatter;
import hexlet.code.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StylishFormatter implements Formatter {

    public static final int INDENT_SIZE = 2;

    private String getOperationChar(final Operation operation) {
        return switch (operation) {
            case add -> "+";
            case remove -> "-";
            case same, update -> " ";
        };
    }

    private String formatValue(final JsonNode value) {
        if (value.isTextual()) {
            return value.textValue();
        }
        if (value.isArray()) {
            return formatArray(value);
        }
        if (value.isObject()) {
            return formatObject(value);
        }
        return value.toString();
    }

    private String formatArray(final JsonNode value) {
        var result = new StringBuilder();
        result.append("[");
        var arrayNode = (ArrayNode) value;
        Stream<JsonNode> stream = IntStream
            .range(0, arrayNode.size())
            .mapToObj(arrayNode::get);
        result.append(
            stream.map(this::formatValue).collect(Collectors.joining(", "))
        );
        result.append("]");
        return result.toString();
    }

    private String formatObject(final JsonNode value) {
        var result = new StringBuilder();
        result.append("{");
        var objectNode = (ObjectNode) value;
        List<Map.Entry<String, JsonNode>> entryList = new ArrayList<>();
        objectNode.fields().forEachRemaining(entryList::add);
        var joinResult = entryList
            .stream()
            .map(entry -> String.format(
                "%s=%s",
                entry.getKey(),
                formatValue(entry.getValue())
            ))
            .collect(Collectors.joining(", "));
        result.append(joinResult);
        result.append("}");
        return result.toString();
    }

    @Override
    public String format(final List<DiffEntry> json) {
        var builder = new StringBuilder();
        builder.append("{\n");
        for (var entry : json) {
            var indent = " ".repeat(INDENT_SIZE);
            var name = entry.key();
            var value = formatValue(entry.value());
            var newValue =
                entry.newValue() != null ? formatValue(entry.newValue())
                    : null;
            if (entry.isUpdate()) {
                builder.append(
                    String.format("%s%s %s: %s%n", indent,
                        getOperationChar(Operation.remove), name,
                        value)
                );
                builder.append(
                    String.format("%s%s %s: %s%n", indent,
                        getOperationChar(Operation.add), name,
                        newValue)
                );
            } else {
                var operation = getOperationChar(entry.operation());
                builder.append(
                    String.format("%s%s %s: %s%n", indent, operation, name,
                        value)
                );
            }
        }
        builder.append("}");

        return builder.toString();
    }
}
