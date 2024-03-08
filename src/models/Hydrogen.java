package models;

import java.io.IOException;

public class Hydrogen {
    private int hydrogens;

    /**
     * Default Hydrogen Constructor
     * @param hydrogens
     */
    public Hydrogen(int hydrogens) {
        this.hydrogens = hydrogens;
    }

    public static void main(String[] args) throws IOException {
        Console console = new Console();

        int hydrogens = console.input("Enter the number of Hydrogen atoms: ").nextInt();

        // Create a new Hydrogen
        new Hydrogen(hydrogens);
    }
}
