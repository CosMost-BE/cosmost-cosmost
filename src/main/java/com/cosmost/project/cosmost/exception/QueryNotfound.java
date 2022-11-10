package com.cosmost.project.cosmost.exception;

public class QueryNotfound extends IllegalArgumentException {
    public static final String MESSAGE = "입력된 쿼리값이 올바르지 않습니다.";

    public QueryNotfound() {
        super(MESSAGE);
    }
}
