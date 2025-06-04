package org.bpm.abcbook.util;

import jakarta.persistence.Query;

import java.util.Iterator;
import java.util.List;

public class DbUtil {

    public static String createInQuery(String prefix, List list) {
        StringBuilder inQuery = new StringBuilder(" IN (");

        for (int i = 0; i < list.size(); i++) {
            inQuery.append(",:").append(prefix).append(i);
        }
        inQuery.append(")");
        inQuery = new StringBuilder(inQuery.toString().replaceFirst(",", ""));
        return inQuery.toString();
    }

    public static void setParamInQuery(Query query, String prefix, List list) {
        int idx = 0;
        Iterator var = list.iterator();

        while (var.hasNext()) {
            Object item = var.next();
            query.setParameter(prefix + idx++, item);
        }
    }
}
