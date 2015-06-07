package jreader.web.controller.ajax;

import javax.servlet.http.HttpServletResponse;

import jreader.dto.StatusDto;
import jreader.services.ServiceException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public StatusDto handleServiceException(ServiceException e, HttpServletResponse response) {
        response.setStatus(e.getStatus().getCode());
        return new StatusDto(1, e.getMessage());
    }

}
