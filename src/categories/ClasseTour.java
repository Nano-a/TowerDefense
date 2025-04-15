package categories;

public enum ClasseTour {
    MAGE("Mage"), ARTILLARY("Artillary"), SNIPER("Sniper"), BASIC("Basic"), RAPID_FIRE("Archer");

    private String name;

    ClasseTour(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
