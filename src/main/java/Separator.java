public enum Separator implements IAtomTable {
    OPEN_BRACES("{",200),
    CLOSED_BRACES("}",201),
    OPEN_BRACKETS("[",202),
    CLOSED_BRACKETS("]",203),
    OPEN_PARENTHESIS("(",204),
    CLOSED_PARENTHESIS(")",205),
    SPACE(" ",206),
    COMMA(",",207),
    SEMICOLON(";",208),
    COLON(":",209);

    private final String separator;
    private final int code;

    Separator(String separator, int code) {
        this.separator = separator;
        this.code = code;
    }

    public String getSeparator() {
        return separator;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
