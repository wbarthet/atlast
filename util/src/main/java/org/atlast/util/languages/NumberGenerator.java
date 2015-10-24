package org.atlast.util.languages;

import java.util.Random;

/**
 * Created by wbarthet on 9/27/15.
 */
public class NumberGenerator {

    public static String[] NUMBERS = new String[] {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "twenty-two", "twenty-seven", "forty-eight", "thirty-three", "thurty-six", "forty-two"};

    public static String generateNumber() {
        Random r = new Random();

        int index = r.nextInt(NUMBERS.length);

        return NUMBERS[index];
    }
}
