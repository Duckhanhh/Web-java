package org.bpm.abcbook;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Const {
    public static class BookCategory {
        public static final List<String> BOOK_CATEGORY = Arrays.asList("TIEU_THUYET", "TRUYEN_NGAN", "TRUYEN_TRANH", "TRUYEN_THIEU_NHI");
    }

    public static final Map<String, String> BOOK_CATEGORY_LABELS = Map.of(
        "TIEU_THUYET", "Tiểu thuyết",
        "TRUYEN_NGAN", "Truyện ngắn",
        "TRUYEN_TRANH", "Truyện tranh",
        "TRUYEN_THIEU_NHI", "Truyện thiếu nhi"
    );

    public static final Map<String, String> SUPPLIER_LABELS = Map.of(
        "SUPPLIER1", "Alpha Books",
        "SUPPLIER2", "Beta Books",
        "SUPPLIER3", "Gamma Books"
    );
}
