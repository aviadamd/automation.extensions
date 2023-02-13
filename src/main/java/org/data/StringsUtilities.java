package org.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import java.util.regex.Pattern;
import static org.data.StringsRegexConstants.*;

@Slf4j
public class StringsUtilities extends NumberUtils {

    public static String splitString(String primaryString, String regexSplit, int index) {
        String [] stringSpilt = primaryString.split(regexSplit);
        if (stringSpilt.length == 0) {
            return primaryString;
        }
        return stringSpilt[index];
    }

    public static String cleanWhiteSpaces(String value) {
        try {
            return value.replace(ANY_NUMBER_OF_WHITE_CHARACTERS, "");
        } catch (Exception exception) {
            return "";
        }
    }

    public static int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception exception) {
            return 0;
        }
    }

    public static double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception exception) {
            return 0.0;
        }
    }

    public static boolean genericMatcher(String value, String fromPattern) {
        try {
            Pattern pattern = Pattern.compile(fromPattern);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Pattern pattern = Pattern.compile(DECIMAL_PATTERN);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        try {
            Pattern pattern = Pattern.compile(NUMERIC_CHARS);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isContainsHebrewLetters(String value) {
        try {
            Pattern pattern = Pattern.compile(HEBREW_WORDS);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isContainsLetters(String value) {
        try {
            Pattern pattern = Pattern.compile(LETTERS_CHARS);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isCreditCard(String value) {
        try {
            return GenericValidator.isCreditCard(value);
        } catch (Exception exception) {
            return false;
        }
    }

}
