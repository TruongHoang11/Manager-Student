package vn.hoangtojava.student_management.dto.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e, WebRequest request){
        System.out.println("========================> handleValidationException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));


        String errorMessage = e.getMessage();
        // neu exception la MethodArgumentNotValidException
        if(e instanceof MethodArgumentNotValidException){
            int start = errorMessage.lastIndexOf("[");
            int end = errorMessage.lastIndexOf("]") ;
            errorMessage = errorMessage.substring(start + 1, end - 1);

            errorResponse.setError("Payload invalid");
        } else if(e instanceof ConstraintViolationException ){
            errorMessage = errorMessage.substring(errorMessage.indexOf(" ") + 1);
            errorResponse.setError("Parameter invalid");
        }
        errorResponse.setMessage(errorMessage);



        return errorResponse;

    }
}
