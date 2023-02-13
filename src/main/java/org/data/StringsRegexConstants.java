package org.data;

public class StringsRegexConstants {

    public static final String NUMERIC_CHARS_WITH_DOT_AND_HYPHEN =     "[^0-9.,]";
    public static final String NUMERIC_CHARS_WITH_DOT =                 "[^0-9.]";
    public static final String NUMERIC_CHARS_WITH_DOT_AND_MINUS =      "[^0-9-.]";
    public static final String NUMERIC_CHARS_WITH_MINUS =              "[^0-9-.]";
    public static final String NUMERIC_CHARS_WITH_MINUS_AND_PLUS =    "[^0-9-+.]";
    public static final String NUMERIC_CHARS_WITHS_MINUS_AND_PLUS =   "[^(0-9-.+|0-9-\u200E+.)]";

    public static final String HEBREW_PATTERN_FIX =   "[^(\u200F*)|(\u200E*)|(\uE002*)]";
    public static final String NUMERIC_CHARS =                           "[^0-9]";
    public static final String LETTERS_CHARS =                     "[^a-zA-Zא-ת]";
    public static final String HEBREW_WORDS =                    ".*[\u0590-\u05ff]+.";
    public static final String DECIMAL_PATTERN =                 "([0-9]*)\\.([0-9]*)";
    public static final String LETTERS_CHARS_WITH_UPPER_HYPHEN =  "[^׳a-zA-Zא-ת]";
    public static final String ANY_NUMBER_OF_WHITE_CHARACTERS =                  "\\s";
    public static final String UNTIL_FIRST_HYPHEN =                         "-.*";
    public static final String AFTER_FIRST_DOT =                          ".*\\.";
    public static final String ONLY_COIN_SIGN =                         "[^₪$€$]";
    public static final String LEFT_TO_RIGHT =                         "\uE002";
    public static final String LEFT_TO_RIGHT_MARK_E =                         "\u200E";
    public static final String LEFT_TO_RIGHT_MARK_F =                         "\u200F";
    public static final String WHITE_SPACES =                                   "\\s+";
    public static final String HYPHEN =                                            "-";
    public static final String DOT =                                               ".";
    public static final String DOT_CHAR =                                        "\\.";
    public static final String SLASH =                                             "/";
    public static final String COMMA =                                             ",";
    public static final String EMPTY_STRING =                                       "";
    public static final String SPACE =                                             " ";
    public static final String PLUS =                                              "+";
    public static final String SHEKEL_SIGN =                                       "₪";
    public static final String DOLLAR_SIGN =                                       "$";

}
