package Model;

public enum Keyword implements IAtomTable {
    VAR(10),
    INTEGER(11),
    REAL(12),
    READ(13),
    WRITE(14),
    IF(15),
    THEN(16),
    ELSE(17),
    WHILE(18),
    DO(19);

    private final int code;

    Keyword(int code) {
        this.code = code;
    }

    @Override
    public int getCode(){
        return this.code;
    }
}
