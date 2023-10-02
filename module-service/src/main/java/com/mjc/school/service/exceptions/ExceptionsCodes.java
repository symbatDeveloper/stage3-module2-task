package com.mjc.school.service.exceptions;

public enum ExceptionsCodes {
    NEWS_ID_DOES_NOT_EXIST("01", "News with id %d does not exist."),
    AUTHOR_ID_DOES_NOT_EXIST("02", "Author Id does not exist. Author Id is: %s"),
    VALIDATE_NEGATIVE_OR_NULL_NUMBER(
            "03", "%s can not be null or less than 1. %s is: %s"),
    VALIDATE_STRING_LENGTH(
            "04", "%s can not be less than %d and more than %d symbols. %s is %s");

    private final String errorMessage;

    ExceptionsCodes(String errorCode, String message) {
        this.errorMessage = "ERROR_CODE: " + errorCode + " ERROR_MESSAGE: " + message;
    }

    public String getMessage() {
        return errorMessage;
    }
}
