package br.com.tairoroberto.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tairo on 24/01/15.
 */
public class ValidaHora {
    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private static Pattern pattern;
    private static Matcher matcher;

    /**
     * Validate time in 24 hours format with regular expression
     * @param time time address for validation
     * @return true valid time fromat, false invalid time format
     */
    public static boolean validate(final String time){
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
        matcher = pattern.matcher(time);
        return matcher.matches();
    }
}
