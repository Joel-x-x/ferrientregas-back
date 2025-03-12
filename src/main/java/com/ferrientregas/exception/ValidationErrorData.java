package com.ferrientregas.exception;

import java.util.List;

public record ValidationErrorData(
        String field,
        List<String> messages
) {
}
