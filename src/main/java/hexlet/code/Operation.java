package hexlet.code;

public enum Operation {
    equal,
    add,
    remove;

    /**
     * Operation in string.
     *
     * @return String
     */
    public String toString() {
        return switch (this) {
            case add -> "+";
            case remove -> "-";
            case equal -> "";
        };
    }
}
