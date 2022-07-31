package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;

public record DiffEntry(String key, JsonNode value, JsonNode newValue,
                        Operation operation) {

    public static DiffEntry same(final String key,
                                 final JsonNode value) {
        return new DiffEntry(key, value, null, Operation.same);
    }

    public static DiffEntry add(final String key,
                                final JsonNode value) {
        return new DiffEntry(key, value, null, Operation.add);
    }

    public static DiffEntry remove(final String key,
                                   final JsonNode value) {
        return new DiffEntry(key, value, null, Operation.remove);
    }

    public static DiffEntry update(final String key,
                                   final JsonNode value,
                                   final JsonNode newValue) {
        return new DiffEntry(key, value, newValue, Operation.update);
    }

    public boolean isUpdate() {
        return operation.equals(Operation.update);
    }
}
