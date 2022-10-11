package com.cosmost.project.cosmost.exception;

public class CourseIdNotfound extends IllegalArgumentException {
    public static final String MESSAGE = "해당 코스는 없습니다.";

    public CourseIdNotfound() {
        super(MESSAGE);
    }
}
