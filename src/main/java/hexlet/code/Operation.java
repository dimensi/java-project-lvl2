package hexlet.code;

public enum Operation {
    equal,
    add,
    remove;

    public String toString() {
        return switch (this) {
            case add -> "+";
            case remove -> "-";
            case equal -> "";
        };
    }
}
