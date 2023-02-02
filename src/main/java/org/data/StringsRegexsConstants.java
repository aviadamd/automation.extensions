package org.data;

public class StringsRegexsConstants {

    public static final String SAVE_NUMERIC_CHARS_WITH_DOT_AND_HYPHEN =     "[^0-9.,]";
    public static final String SAVE_NUMERIC_CHARS_WITH_DOT =                 "[^0-9.]";
    public static final String SAVE_NUMERIC_CHARS_WITH_DOT_AND_MINUS =      "[^0-9-.]";
    public static final String SAVE_NUMERIC_CHARS_WITH_MINUS =              "[^0-9-.]";
    public static final String SAVE_NUMERIC_CHARS_WITH_MINUS_AND_PLUS =    "[^0-9-+.]";
    public static final String SAVE_NUMERIC_CHARS_WITHS_MINUS_AND_PLUS =   "[^(0-9-.+|0-9-\u200E+.)]";
    public static final String SAVE_NUMERIC_CHARS =                           "[^0-9]";
    public static final String SAVE_NUMERIC_CHARS1 =                           "\\D+";
    public static final String SAVE_LETTERS_CHARS =                     "[^a-zA-Zא-ת]";
    public static final String SAVE_LETTERS_CHARS_WITH_UPPER_HYPHEN =  "[^׳a-zA-Zא-ת]";
    public static final String ANY_NUMBER_OF_WHITE_CHARACTERS =                  "\\s";
    public static final String SAVE_UNTIL_FIRST_HYPHEN =                         "-.*";
    public static final String SAVE_AFTER_FIRST_DOT =                          ".*\\.";
    public static final String SAVE_ONLY_COIN_SIGN =                         "[^₪$€$]";
    public static final String HEBREW_LEFT_TO_RIGHT =                         "\uE002";
    public static final String LEFT_TO_RIGHT_MARK =                           "\u200E";
    public static final String SAVE_NUMERIC_CHARS_ALL =                    "[^0-9.,₪]";

    public static final String HYPHEN =                                            "-";
    public static final String DOT =                                               ".";
    public static final String DOT_CHAR =                                         "\\.";
    public static final String SLASH =                                             "/";
    public static final String COMMA =                                             ",";
    public static final String EMPTY_STRING =                                       "";
    public static final String SPACE =                                             " ";
    public static final String PLUS =                                              "+";
    public static final String SHEKEL_SIGN =                                       "₪";
    public static final String DOLLAR_SIGN =                                       "$";
    public static final String EURO_SIGN =                                         "$";
    public static final String GPB_SIGN =                                          "£";
    public static final String RUB_SIGN =                                     "\u20BD";
    public static final String THB_SIGN =                                          "฿";

}
