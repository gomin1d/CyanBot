package me.xjcyan1de.cyanbot.utils;

import static org.apache.commons.lang3.StringUtils.*;

public class StringUtilsMy {

    public static final int INDEX_NOT_FOUND = -1;

    /**
     * @see org.apache.commons.lang3.StringUtils
     */
    public static String substringBetweenIrnoreCase(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = indexOfIgnoreCase(str, open);
        if (start != INDEX_NOT_FOUND) {
            final int end = indexOfIgnoreCase(str, close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * @see org.apache.commons.lang3.StringUtils
     */
    public static String substringAfterLastIgnoreCase(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        final int pos = lastIndexOfIgnoreCase(str, separator);
        if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }
}
