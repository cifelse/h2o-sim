package models;

import java.io.IOException;

public class Hydrogen {
    private int hydrogens;

    /**
     * Default Oxygen Constructor
     * @param hydrogens
     */
    public Hydrogen(int hydrogens) {
        this.hydrogens = hydrogens;
    }

    public static void main(String[] args) throws IOException {
        Console console = new Console();

        int hydrogens = console.input("Enter the number of Oxygen atoms: ").nextInt();

        // Create a new Oxygen
        new Hydrogen(hydrogens);
    }
}
