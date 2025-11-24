package vn.hoangtojava.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(Exception e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        System.out.println("============> handleValidationException");
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=",""));

        String messgage = e.getMessage();
        if(e instanceof MethodArgumentNotValidException) {
            int start = messgage.lastIndexOf("[");
            int end = messgage.lastIndexOf("]");
            messgage = messgage.substring(start + 1, end - 1);
            errorResponse.setError("Payload Invalid");
        }
        else if(e instanceof ConstraintViolationException){
            int start = messgage.indexOf(" ");
            messgage = messgage.substring(start + 1);
            errorResponse.setError("Parameter Invalid");
        }
        errorResponse.setMessage(messgage);
        return errorResponse;
    }




    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus. INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorValidation(Exception e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        System.out.println("============> handleInternalServerErrorValidation");
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=",""));
        if(e instanceof MethodArgumentTypeMismatchException){
            errorResponse.setMessage("Failed to convert value of type");
            errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        return errorResponse;
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowedValidation(Exception e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        System.out.println("================> handleMethodNotAllowedValidation" );
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        errorResponse.setPath(request.getDescription(false).replace("uri="," "));
        errorResponse.setError(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        String message = e.getMessage();
        int start = message.indexOf(" ");
        errorResponse.setMessage(message.substring(start + 1));
        return errorResponse;
    }
}
