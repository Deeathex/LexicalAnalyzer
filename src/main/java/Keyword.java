public enum Keyword {
    VAR(0),
    INTEGER(1),
    REAL(2),
    READ(3),
    WRITE(4),
    IF(5),
    THEN(6),
    ELSE(7),
    WHILE(8),
    DO(9);

    private final int id;

    Keyword(int id) {
        this.id = id;
    }
}
