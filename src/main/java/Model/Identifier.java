package Model;

public enum Identifier implements IAtomTable {
    IDENTIFIER(0);

    private final int code;

    Identifier(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
