package org.bpm.abcbook;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Const {
    public static class OrderState {
        public static final Long ORDER_STATE_PENDING = 1L; // dang cho tiep nhan
        public static final Long ORDER_STATE_WAIT_FOR_DELIVERY = 2L; // dang cho giao hang
        public static final Long ORDER_STATE_DELIVERING = 3L; // dang giao hang
        public static final Long ORDER_STATE_DELIVERED = 4L; // da giao hang
    }

    public static class OrderStatus {
        public static final Long ORDER_STATUS_IN_PROGRESS = 1L; // dang thuc hien
        public static final Long ORDER_STATUS_COMPLETED = 3L; // da hoan thanh
        public static final Long ORDER_STATUS_CANCELLED = 4L; // da huy
    }

    public static class BookStatus {
        public static final Long BOOK_STATUS_AVAILABLE = 1L;
        public static final Long BOOK_STATUS_UNAVAILABLE = 0L;
    }

    public static class PayStatus {
        public static final Long PAID = 1L;
        public static final Long UNPAID = 0L;
    }

    public static class PayMethod {
        public static final Long IMMEDIATE = 1L;
        public static final Long LATER = 2L;
    }

    public static final List<String> LIST_SHIPPING_CARRIERS = Arrays.asList(
        "Giao Hang Nhanh",
        "Giao Hang Tiet Kiem",
        "Giao Hang Tiet Kiem Nhanh",
        "Giao Hang Tiet Kiem Tuan Tu"
    );
}
