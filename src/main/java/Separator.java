public enum Separator {
    OPEN_BRACES("{"),
    CLOSED_BRACES("}"),
    OPEN_BRACKETS("["),
    CLOSED_BRACKETS("]"),
    OPEN_PARANTHESIS("("),
    CLOSED_PARANTHESIS(")"),
    SPACE(" "),
    COMMA(","),
    SEMICOLON(";"),
    COLON(":");

    private final String separator;

    Separator(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
