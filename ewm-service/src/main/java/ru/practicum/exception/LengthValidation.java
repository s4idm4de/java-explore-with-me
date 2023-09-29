package ru.practicum.exception;

public class LengthValidation {
    public static void validate(int min, int max, String text) throws ValidatedException {
        if (text == null || text.length() > max || text.length() < min) throw new ValidatedException("wrong length");
    }
}
