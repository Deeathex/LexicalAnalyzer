package Model;

public enum LexicalPriority {
    KEYWORD(1),
    OPERATOR(1),
    SEPARATOR(1),
    IDENTIFIER(2),
    CONSTANT(2),
    LEXICAL_ERROR(3);

    private final int priority;

    LexicalPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
