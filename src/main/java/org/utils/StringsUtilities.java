package org.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import static org.utils.StringsRegexConstants.*;

@Slf4j
public class StringsUtilities {
    public String splitString(String primaryString, String regexSplit, int index) {
        String [] stringSpilt = primaryString.split(regexSplit);
        if (stringSpilt.length == 0) {
            return primaryString;
        }
        return stringSpilt[index];
    }

    public List<String> stringToList(String text, String splitBy) {
        try {
            return new ArrayList<>(Arrays.asList(text.split(splitBy)));
        } catch (Exception exception) {
            return new ArrayList<>();
        }
    }

    public String cleanWhiteSpaces(String value) {
        try {
            return value.replace(ANY_NUMBER_OF_WHITE_CHARACTERS, "");
        } catch (Exception exception) {
            return "";
        }
    }

    public int toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception exception) {
            return 0;
        }
    }

    public double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception exception) {
            return 0.0;
        }
    }

    public boolean genericMatcher(String value, String fromPattern) {
        try {
            Pattern pattern = Pattern.compile(fromPattern);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isDouble(String value) {
        try {
            Pattern pattern = Pattern.compile(DECIMAL_PATTERN);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isInteger(String value) {
        try {
            Pattern pattern = Pattern.compile(NUMERIC_CHARS);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isContainsHebrewLetters(String value) {
        try {
            Pattern pattern = Pattern.compile(HEBREW_WORDS);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isContainsLetters(String value) {
        try {
            Pattern pattern = Pattern.compile(LETTERS_CHARS);
            return pattern.matcher(value).find();
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean isCreditCard(String value) {
        try {
            return GenericValidator.isCreditCard(value);
        } catch (Exception exception) {
            return false;
        }
    }

    @SafeVarargs
    public final List<String> search(List<String> searchList, Predicate<String>... predicateList) {
        List<String> collector = new ArrayList<>();
        for (Predicate<String> predicate: predicateList) {
            for (String str: searchList) {
                if (predicate.test(str)) {
                    collector.add(str);
                    break;
                }
            }
        }
        return collector;
    }
    @SafeVarargs
    public final List<String> search(List<String> searchList, Matcher<String>... matchersList) {
        List<String> collector = new ArrayList<>();
        for (Matcher<String> matcher: matchersList) {
            for (String str: searchList) {
                if (matcher.matches(str)) {
                    collector.add(str);
                    break;
                }
            }
        }
        return collector;
    }
}
