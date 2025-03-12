package com.ferrientregas.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResultResponse<Object,String>> error404Handler(
            EntityNotFoundException exc){
       ResultResponse<Object,String> response = ResultResponse.failure(
               Collections.singletonList(exc.getMessage()),
               HttpStatus.NOT_FOUND.value()
       );
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<Object,ValidationErrorData>> error400Handler(
            MethodArgumentNotValidException exc){
        List<FieldError> errors = exc.getFieldErrors();
        List<ValidationErrorData> messages = new ArrayList<>();

        errors.forEach(error->{
            messages.add(new ValidationErrorData(error.getField(),
                    Collections.singletonList(error.getDefaultMessage())));
        });

        ResultResponse<Object, ValidationErrorData> response =
                ResultResponse.failure(messages, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResultResponse<Object,ValidationErrorData>> validationErrorHandler(
            ConstraintViolationException exc
    ){
        Set<ConstraintViolation<?>> violations = exc.getConstraintViolations();
        List<ValidationErrorData> messages = new ArrayList<>();

        violations.forEach(violation->{
            messages.add(new ValidationErrorData(violation.getPropertyPath().toString(),
                    Collections.singletonList(violation.getMessage()))
            );
        });

        ResultResponse<Object, ValidationErrorData> response = ResultResponse.failure(
                messages, HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<Object,String>> error500Handler(Exception exc){
        ResultResponse<Object,String> response = ResultResponse.failure(
                Collections.singletonList("Error interno del servidor: "+
                        exc.getLocalizedMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ResultResponse<Object,String>> error500Handler(
            JpaSystemException exc
    ){
       ResultResponse<Object,String> response = ResultResponse.failure(
               Collections.singletonList("Error en el sistema: "+
                       exc.getLocalizedMessage()),
               HttpStatus.INTERNAL_SERVER_ERROR.value()
       );
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
