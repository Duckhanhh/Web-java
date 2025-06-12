package org.bpm.abcbook.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

public class DataUtil {
    public static boolean isNullOrEmpty(CharSequence cs) {
        if (cs != null && !cs.isEmpty()) {
            for (int i = 0; i < cs.length(); i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static String formatDateToString(Date date) {
        LocalDate localDate = convertToLocalDate(date);
        if (localDate == null) return "";
        DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/yyyy");
        return localDate.format(formatter);
    }

    public static LocalDate convertToLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
