package hexlet.code.formatter;

import hexlet.code.DiffEntry;
import hexlet.code.Formatter;
import hexlet.code.Operation;

import java.util.List;

public class StylishFormatter implements Formatter {
    private final int indentSize;

    public StylishFormatter(int indentSize) {
        this.indentSize = indentSize;
    }

    @Override
    public String format(List<DiffEntry> json) {
        var builder = new StringBuilder();
        builder.append("{\n");
        for (var entry : json) {
            var indent = " ".repeat(indentSize);
            var operation = entry.operation() == Operation.equal ? " " : entry.operation().toString();
            var name = entry.key();
            var value = entry.value();
            builder.append(String.format("%s%s %s: %s%n", indent, operation, name, value));
        }
        builder.append("}");

        return builder.toString();
    }
}
