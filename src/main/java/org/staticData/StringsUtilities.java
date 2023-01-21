package org.staticData;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static org.staticData.MobileRegexConstants.*;

@Slf4j
public class StringsUtilities extends NumberUtils {

    public static String LINE = "=====================================================================================";
    public static String LINE(String stepName)  { return " ========================== ["+stepName+"] ========================== "; }
    public static String LINE1(String stepName) { return " -------------------------- ["+stepName+"] -------------------------- "; }

    /**
     * @param primaryString
     * @param regexSplit
     * @param index
     * @return
     */
    public static String splitString(String primaryString, String regexSplit, int index) {
        String [] stringSpilt = primaryString.split(regexSplit);
        if (stringSpilt.length == 0) {
            return primaryString;
        }
        return stringSpilt[index];
    }

    /**
     * remove white space from string
     * @param line
     * @return
     */
    public static String removeWhiteSpaces(String line) {
        final String linePattern = "\\s+";
        return line.replace(linePattern,"");
    }

    /**
     * return true false is an number is double
     * @param value
     * @return value
     */
    public static boolean isDouble(String value) {
        if (isDigits(value)) {
            final String decimalPattern = "([0-9]*)\\.([0-9]*)";
            return Pattern.matches(decimalPattern, value);
        }
        return false;
    }

    /**
     * return true false is an number is int
     * @param value of str
     * @return value
     */
    public static boolean isInteger(String value) {
        if (isDigits(value)) {
            return Pattern.matches(SAVE_NUMERIC_CHARS, value);
        }
        return false;
    }

    /**
     * return true text left to right
     * @return value
     */
    public static String hebrewTextLeftToRight(String text) {
        return text == null ? "" : text
                .replaceAll(HEBREW_LEFT_TO_RIGHT, EMPTY_STRING)
                .replaceAll(MobileRegexConstants.LEFT_TO_RIGHT_MARK, EMPTY_STRING);
    }

    /**
     * @param strNum free text
     * @return is the string is contains numbers
     */
    public static boolean isContainsNumbers(String strNum) {
        if (strNum == null) return false;
        try {
            return strNum.matches(".*\\d.*") || isDigits(strNum);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param data free text
     * @param match free text match to last char
     * @return fix str without last char
     */
    public static String replaceLastChar(String data, String match) {
        if (data.endsWith(match))
            data = data.substring(0, data.length() -1);
        return data;
    }

    public static int toInt(String strToInt) {
        return toInt(strToInt,0);
    }

    public static int toInt(String strToInt, int defaultValue) {
        if (strToInt == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(strToInt);
            } catch (NumberFormatException var3) {
                log.error(var3.getMessage());
                return defaultValue;
            }
        }
    }

    public static double toDouble(String strToInt, double defaultValue) {
        if (strToInt == null) {
            return defaultValue;
        } else {
            try {
                return Double.parseDouble(strToInt);
            } catch (NumberFormatException var3) {
                log.error(var3.getMessage());
                return defaultValue;
            }
        }
    }

    /**
     * @param str text to test
     * @return true/false if the text is in english
     */
    public static boolean isContainsEnglishLetters(String str) {
        for (char ch : str.toCharArray()) {
            if (!(ch >= 'A' && ch <= 'Z') && !(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param str text to test
     * @return true/false if the text is in english
     */
    public static boolean isContainsHebrewLetters(String str) {
        Pattern pattern = Pattern.compile(".*[\u0590-\u05ff]+.");
        return pattern.matcher(str).find();
    }

    /**
     * @param str text to test
     * @return true/false if the text is in english
     */
    public static boolean isContainsLetters(String str) {
        return (isContainsHebrewLetters(str) || isContainsEnglishLetters(str));
    }

    /**
     * this method remove duplicate chars from string
     * @param text the expected text to work with
     * @return this
     */
    public static String removeDuplicateTexts(String text) {
        LinkedHashSet<Character> set = new LinkedHashSet<>();
        List<String> newString = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            set.add(text.charAt(i));
        }

        for (Character character : set) {
            newString.add(String.valueOf(character));
        }

        return newString.stream().map(String::toString).collect(Collectors.joining());
    }

    /**
     * this method remove duplicate words from string
     * @param text the expected text to work with
     * @return this
     */
    public static String removeDuplicateWords(String text) {
        String[] getText = text.split("\\s+");
        LinkedHashSet<String> setWords = new LinkedHashSet<>(Arrays.asList(getText));
        StringBuilder stringBuilder = new StringBuilder();

        int index = 0;
        for (String data : setWords) {
            if (index > 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(data);
            index++;
        }

        text = stringBuilder.toString();
        return text;
    }

    /**
     * this method remove duplicate words from string
     * @param text the expected text to work with
     * @return this
     */
    public static boolean hasDuplicateWords(String text) {
        String[] getText = text.split("\\s+");
        LinkedHashSet<String> setWords = new LinkedHashSet<>(Arrays.asList(getText));
        StringBuilder stringBuilder = new StringBuilder();

        int index = 0;
        for (String data : setWords) {
            if (index > 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(data);
            index++;
        }

        return stringBuilder.toString().length() == getText.length;
    }

    /**
     * Just for clean strings
     * @param name string for clean
     * @param strToClean the string need to be removed
     * @return fix name
     */
    public static String replaceTexts(String name, List<String> strToClean) {
        for (String text : strToClean) {
            if (!text.isEmpty()) {
                name = name.replace(text, "");
            }
        }
        return name;
    }

    /** string preparation **/
    public static String cleanStringChars(String name) {
        name = name.replaceAll("%","")
                .replaceAll("@","");
        name = name.replace("?","");
        return name;
    }

    public static String setNewPassword(String charsAfterTheVn) {
        //Return random string for pass vn....
        return String.format("%0" + charsAfterTheVn + "d", new Random().nextInt(100000));
    }

    public static char randomChar(int bounds) {
        //Return random char
        return (char) (new Random().nextInt(bounds) + 'a');
    }

    public static String formatStringToNumber(String number, String charToReplace, String regexToReplace) {
        if (number.contains(charToReplace)) {
            number = number.replaceAll(charToReplace, EMPTY_STRING);
        }

        number = number.replaceAll(regexToReplace, EMPTY_STRING);
        return number;
    }

    public static boolean isCreditCard(String value) {
        return GenericValidator.isCreditCard(value);
    }

    public static String takeStringValueUntilChar(String fullString, char toCharValue) {
        StringBuilder builder = new StringBuilder();

        for (char a : fullString.toCharArray()) {
            builder.append(a);
            if (a == toCharValue) break;
        }

        return builder.toString();
    }

    public static String getAmount(int rangeFrom, int rangeTo) {
        int value = new Random().nextInt((rangeTo - rangeFrom) + 1);
        return String.valueOf(value);
    }

    public static String addZerosAfterDot(String strParam) {
        //get the index of the dot inside the str
        int pos = strParam.indexOf(".");
        //if the pos index is -1 add .00 to the str
        if (pos == -1) {
            strParam = strParam + ".00";
        } else {
            //generate new str with fix .00 str
            String beforeDot = strParam.substring(0, pos);
            StringBuilder afterDot = new StringBuilder(strParam.substring(pos + 1));
            //add the new two chars 0 after the dot
            if (afterDot.length() < 2) afterDot.append("0");
            //new str
            strParam = beforeDot + "." + afterDot;
        }

        return strParam;
    }
}
