/**
 * A custom Console class to easily access the cmd/console
 * @author Louis Lemsic
 * @version 1.0.0
 */

package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Console class for easy logging, user input, and clearing the console.
 */
public class Console {
    // Body Limit if there are Headers
    private int OVERFLOW = 20;

    // Name of the Console
    private String name;

    // Scanner for User Inputs in the Console
    private final Scanner sc;

    // Time Tracker
    private long millis;

    // Array of Strings to be sent on the top always
    private ArrayList<Object> headers, body;

    /**
     * Default Console Constructor
     */
    public Console() {
        this.sc = new Scanner(System.in);
        this.headers = new ArrayList<Object>();
        this.body = new ArrayList<Object>();
        this.millis = System.nanoTime();
        this.name = "";

        try {
            clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Console Constructor with name
     * @param name - the name of the Console
     */
    public Console(String name) {
        this.sc = new Scanner(System.in);
        this.headers = new ArrayList<Object>();
        this.body = new ArrayList<Object>();
        this.millis = System.nanoTime();
        this.name = name;

        try {
            clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Console Constructor with name and scanner
     * @param name - the name of the Console
     * @param sc - the Scanner Object
     */
    public Console(String name, Scanner sc) {
        this.name = name;
        this.sc = sc;
        this.millis = System.nanoTime();
        
        try {
            clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a timestamp
     * @return timestamp
     */
    public String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss"));
    }

    public String getTimeStamp(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Get the name of the Console
     * @return the name of the Console
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the Console
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The main function for getting an input from the user
     * @param prompt - question to prompt to the user
     * @return the input of the user
     */
    public Scanner input(String prompt) {
        System.out.print(formatMessage(prompt));

        if (!this.headers.isEmpty()) {
            addBody(formatMessage(prompt) + "[Protected]");
        }

        return this.sc;
    }

    /**
     * Start the internal timer for tracking
     */
    public void startTime() {
        this.millis = System.nanoTime();
    }

    /**
     * End the internal timer for tracking and return the result
     * @return - time elapsed
     */
    public String endTime() {
        long time = (long) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.millis);

        return time < 10000 ? time + " milliseconds" : TimeUnit.MILLISECONDS.toSeconds(time) + " seconds";
    }

    /**
     * Clear the console
     */
    public void clear()  {
        try {
            System.out.print("\033[H\033[2J");
            Runtime.getRuntime().exec("clear");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Print a Title
     * @param title - The Title
     */
    public void title(String title) {
        System.out.println("██████████████████ " + title + " ██████████████████\n");
    }

    /**
     * Add to the Body ArrayList
     * @param body - The Body String
     */
    private void addBody(String body) {
        if (this.body.size() >= OVERFLOW) this.body.remove(0);

        this.body.add(body + "\n");
    }

    /**
     * Add a header to print in every log
     * @param header - The Header String
     */
    public void addHeader(String header) {
        this.headers.add(header + "\n");
    }

    /**
     * Remove a header in the List of Headers
     * @param header - The Header String
     */
    public void removeHeader(String header) {
        this.headers.remove(header);
    }

    /**
     * Remove a header in the List of Headers using the index
     * @param index - The index of the String
     */
    public void removeHeader(int index) {
        this.headers.remove(index);
    }

    /**
     * The main print statement when Headers are present.
     */
    private void print() {
        clear();

        for (int i = 0; i < this.headers.size(); i++)
            System.out.print(this.headers.get(i));

        System.out.println();    

        for (int i = 0; i < this.body.size(); i++)
            System.out.print(this.body.get(i));
    }

    /**
     * Create a breakline
     */
    public void log() {
        if (this.headers.isEmpty()) {
            System.out.println();
        }
        else {
            addBody("\n");
            print();
        }
    }

    /**
     * Logs a message to the console with optional formatting.
     * @param message - The message to log.
     * @param args - Arguments for string formatting (similar to printf).
     */
    public void log(String message, Object... args) {
        if (this.headers.isEmpty()) {
            System.out.printf("%s\n", String.format(formatMessage(message), args));
        }
        else {
            addBody(String.format(formatMessage(message), args));
            print();
        }
    }

    /**
     * Logs a value of any data type to the console.
     * @param value - The value to log.
     */
    public void log(Object value) {
        if (this.headers.isEmpty()) {
            System.out.printf("%s\n", formatMessage((String) value));
        }
        else {
            addBody(formatMessage((String) value));
            print();
        }
    }

    /**
     * Logs the values of an ArrayList
     * @param al - ArrayList
     */
    public void log(ArrayList<Integer> al) {
        String arrayString = al.stream().map(Object::toString).collect(Collectors.joining(", "));

        if (this.headers.isEmpty()) {
            System.out.printf("%s\n", arrayString);
        }
        else {
            addBody(arrayString);
            print();
        }
    }

    /**
     * Logs the values of an ArrayList
     * @param al - ArrayList
     * @param delimiter - the string that separates each element of the ArrayList when logging
     */
    public void log(ArrayList<Integer> al, String delimiter) {
        String arrayString = al.stream().map(Object::toString).collect(Collectors.joining(delimiter));

        if (this.headers.isEmpty()) {
            System.out.printf("%s\n", arrayString);
        }
        else {
            addBody(arrayString);
            print();
        }
    }

    /**
     * The main function that is responsible in the format of the console logs
     * @param message String to be formatted
     * @return - the formatted String
     */
    private String formatMessage(String message) {
        if (message.length() > 0 && message.charAt(0) == '[')
            return message;

        String header = this.name.length() < 1 ? "" : "[" + name + "]: ";

        // Only check \n in the first element of the string:
        if (message.length() > 0 && message.charAt(0) == '\n')
            return (message.contains("\n") ? "\n" + header : header) + message.replaceFirst("\n", "");

        return header + message;
    }
}