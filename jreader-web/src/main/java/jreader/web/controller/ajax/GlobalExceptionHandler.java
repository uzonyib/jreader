package jreader.web.controller.ajax;

import javax.servlet.http.HttpServletResponse;

import jreader.services.ServiceException;
import jreader.services.exception.ResourceAlreadyExistsException;
import jreader.services.exception.ResourceNotFoundException;
import jreader.web.controller.ResponseEntity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity handleServiceException(final ServiceException e, final HttpServletResponse response) {
        response.setStatus(e.getStatus().value());
        return new ResponseEntity(e.getStatus().value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public org.springframework.http.ResponseEntity<String> handleIllegalArgumentException(final Exception e) {
        return new org.springframework.http.ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public org.springframework.http.ResponseEntity<String> handleNotFoundException(final Exception e) {
        return new org.springframework.http.ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public org.springframework.http.ResponseEntity<String> handleAlreadyExistsException(final Exception e) {
        return new org.springframework.http.ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<String> handleException(final Exception e) {
        return new org.springframework.http.ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
