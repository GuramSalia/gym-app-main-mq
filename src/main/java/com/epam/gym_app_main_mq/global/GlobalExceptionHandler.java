package com.epam.gym_app_main_mq.global;

import com.epam.gym_app_main_mq.exception.*;
import io.micrometer.core.instrument.Counter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final CorrelationIDHandler correlationIDHandler;
    private final EndpointFailCounter endpointFailCounter;

    @Autowired
    public GlobalExceptionHandler(
            CorrelationIDHandler correlationIDHandler,
            EndpointFailCounter endpointFailCounter
    ) {
        this.correlationIDHandler = correlationIDHandler;
        this.endpointFailCounter = endpointFailCounter;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            WebRequest request
    ) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String finalErrorMessage = getErrorMessagesFromFieldErrors(fieldErrors);

        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.BAD_REQUEST,
                finalErrorMessage,
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler" + request.getParameterMap() + "\n");
        logRestDetails(ResponseEntity.badRequest().body(errorDetails));
        incrementCounter();
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleAccessDeniedException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler> handleUsernameNotFoundException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(
            BadCredentialsException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleBadCredentialsException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleInsufficientAuthenticationException(
            InsufficientAuthenticationException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleInsufficientAuthenticationException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleAccountStatusException(
            AccountStatusException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleAccountStatusException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleInvalidBearerTokenException(
            InvalidBearerTokenException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleInvalidBearerTokenException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleUnauthorizedException(
            UnauthorizedException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleUnauthorizedException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UseHasNoAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ErrorDetails> handleUseHasNoAccessException(
            UseHasNoAccessException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleUseHasNoAccessException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ErrorDetails> handleUserNotAuthenticatedException(
            UserNotAuthenticatedException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleUserNotAuthenticatedException"
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UserBlockedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "user is blocked")
    })
    public ResponseEntity<ErrorDetails> handleUserBlockedException(
            UserBlockedException ex,
            WebRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getDescription(false)
        );

        log.info("\n\n>> from error handler > handleUserBlockedException: "
                         + request.getDescription(false) + "\n");
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UserNotCreatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ErrorDetails> handleUserNotCreatedException(
            UserNotCreatedException ex, WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleUserNotCreatedException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );

        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(TrainingNotCreatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ErrorDetails> handleTrainingNotCreatedException(
            TrainingNotCreatedException ex,
            WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleTrainingNotCreatedException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );

        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UserNotUpdatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ErrorDetails> handleUserNotUpdatedException(
            UserNotUpdatedException ex, WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleUserNotUpdatedException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );

        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ErrorDetails> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleUserNotFoundException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );

        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(TrainingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ErrorDetails> handleTrainingNotFoundException(
            TrainingNotFoundException ex,
            WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleTrainingNotFoundException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );

        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(UserNotDeletedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ErrorDetails> handleUserNotDeletedException(
            UserNotDeletedException ex, WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleUserNotDeletedException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );

        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    @ExceptionHandler(Exception.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public final ResponseEntity<ErrorDetails> handleAllException(
            Exception ex, WebRequest request
    ) {
        log.info("\n\n>> from error handler > handleAllException \n");
        ErrorDetails errorDetails = new ErrorDetails(
                correlationIDHandler.getCorrelationId(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );
        ResponseEntity<ErrorDetails> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
        logRestDetails(body);
        incrementCounter();
        return body;
    }

    private String getErrorMessagesFromFieldErrors(List<FieldError> fieldErrors) {
        StringBuilder errorMessage = new StringBuilder();
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            errorMessage.append((i + 1)).append(".").append(error.getDefaultMessage());
            if (i < fieldErrors.size() - 1) {
                errorMessage.append(", ");
            }
        }
        return errorMessage.toString();
    }

    private void incrementCounter() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpRequest = attributes.getRequest();
        String uri = httpRequest.getRequestURI();

        String method = httpRequest.getMethod();
        log.info("\n\n>> from incrementCounter >> GlobalExceptionHandler >> \n> uri: {}\n> method: {}\n", uri, method);
        Counter counter = endpointFailCounter.getCounterByMethodAndUri(method, uri);
        if (counter == null) {
            log.info("\n\n>> no counter found for method/uri: {}/{}\n", method, uri);
        } else {
            counter.increment();
        }
    }

    //logging for task 17.2 when there are errors
    private void logRestDetails(ResponseEntity<ErrorDetails> responseEntity) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("\n\n- REST URI: {}\n-Request method: {}\n", request.getRequestURI(), request.getMethod());

            // Logging request parameters
            log.info("\n\n-\tRequest parameters:");
            request.getParameterMap().forEach((name, values) -> {
                log.info("\n\n-\t{}: {}\n", name, values);
            });

            // Logging response details
            if (responseEntity != null) {
                log.info("\n\n- Response Code: {}\n- Response Body: {}\n",
                         responseEntity.getStatusCode().value(),
                         responseEntity.getBody());
                if (responseEntity.getBody() != null) {
                    log.info("\n\n-\tMessage: {}\n", responseEntity.getBody().getMessage());
                }
            }
        }
    }
}
