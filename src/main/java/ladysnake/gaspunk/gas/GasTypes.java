package ladysnake.gaspunk.gas;

public enum GasTypes implements IGasType {
    GAS(true), SMOKE(false), VAPOR(false);

    private boolean isToxic;

    GasTypes(boolean isToxic) {
        this.isToxic = isToxic;
    }

    public boolean isToxic() {
        return isToxic;
    }

    @Override
    public int getId() {
        return ordinal();
    }


}
