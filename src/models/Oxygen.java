package models;

import java.io.IOException;

public class Oxygen {
    private int oxygens;

    /**
     * Default Oxygen Constructor
     * @param oxygens
     */
    public Oxygen(int oxygens) {
        this.oxygens = oxygens;
    }

    public static void main(String[] args) throws IOException {
        Console console = new Console();

        int oxygens = console.input("Enter the number of Oxygen atoms: ").nextInt();

        // Create a new Oxygen
        new Oxygen(oxygens);
    }
}
