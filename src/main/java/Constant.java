public enum Constant implements IAtomTable {
    CONSTANT(1);

    private final int code;

    Constant(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
