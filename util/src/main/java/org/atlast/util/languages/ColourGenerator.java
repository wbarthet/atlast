package org.atlast.util.languages;

import java.util.Random;

/**
 * Created by wbarthet on 9/27/15.
 */
public class ColourGenerator {

    public static String[] COLOURS = new String[] {"red", "blue", "green", "yellow", "purple", "black", "white", "grey"};

    public static String generateColour() {
        String[] letters = "0123456789ABCDEF".split("");
        String code = "#";
        for (int i = 0; i < 6; i++) {
            Random r = new Random();

            int index = r.nextInt(15);

            code += letters[index];
        }
        return code;
    }

    public static String generateColourName() {
        Random r = new Random();

        int index = r.nextInt(COLOURS.length);

        return COLOURS[index];
    }
}
