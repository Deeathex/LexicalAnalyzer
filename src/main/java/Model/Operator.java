package Model;

public enum Operator implements IAtomTable {
    ADDITION("+",100),
    SUBTRACTION("-",101),
    MULTIPLICATION("*",102),
    DIVISION("/",103),
    ASSIGNMENT("<-",104),
    LESS_THAN("<",105),
    LESS_THAN_OR_EQUAL("<=",106),
    EQUAL("=",107),
    GREATER_THAN_OR_EQUAL(">=",108),
    GREATER_THAN(">",109),
    NOT("NOT",110),
    AND("AND",111),
    OR("OR",112);

    private final String operator;
    private final int code;

    Operator(String operator, int code) {
        this.operator = operator;
        this.code = code;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
