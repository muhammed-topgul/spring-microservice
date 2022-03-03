package com.muhammedtopgul.beerservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author muhammed-topgul
 * @since 22.02.2022 12:50
 */

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> validationErrorHandler(ConstraintViolationException ex) {
        List<String> errorsList = new ArrayList<>(ex.getConstraintViolations().size());

        ex.getConstraintViolations().forEach(error -> errorsList.add(error.toString()));

        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

}
