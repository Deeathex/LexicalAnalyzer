public enum AtomTable {
    ID(0),
    CONST(1),
    VAR(2),
    INTEGER(3),
    REAL(4),
    READ(5),
    WRITE(6),
    IF(7),
    THEN(8),
    ELSE(9),
    WHILE(10),
    DO(11),
    ADDITION(12),
    SUBTRACTION(13),
    MULTIPLICATION(14),
    DIVISION(15),
    ASSIGNMENT(16),
    LESS_THAN(17),
    LESS_THAN_OR_EQUAL(18),
    EQUAL(19),
    GREATER_THAN_OR_EQUAL(20),
    GREATER_THAN(21),
    NOT(22),
    AND(23),
    OR(24),
    OPEN_BRACES(25),
    CLOSED_BRACES(26),
    OPEN_BRACKETS(27),
    CLOSED_BRACKETS(28),
    OPEN_PARANTHESIS(29),
    CLOSED_PARANTHESIS(30),
    SPACE(31),
    COMMA(32),
    SEMICOLON(33),
    COLON(34);

    private final int atomCode;

    AtomTable(int atomCode) {
        this.atomCode = atomCode;
    }

    public int getAtomCode() {
        return atomCode;
    }
}
