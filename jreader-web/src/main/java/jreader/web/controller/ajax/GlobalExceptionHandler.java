package jreader.web.controller.ajax;

import javax.servlet.http.HttpServletResponse;

import jreader.services.ServiceException;
import jreader.web.controller.ResponseEntity;

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

}
