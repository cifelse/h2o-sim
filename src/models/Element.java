package models;

/**
 * A special type of ArrayList made for Elements or Atoms
 */
public class Element {
    // Element Symbol
    private final String name;
    // Bonded Status
    private boolean isBonded;

    public Element(String name) {
        this.name = name;
        this.isBonded = false;
    }

    public String getName() {
        return this.name;
    }

    public boolean isBonded() {
        return this.isBonded;
    }

    public void bond() {
        this.isBonded = true;
    }
}
