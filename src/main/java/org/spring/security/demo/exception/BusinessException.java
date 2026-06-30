package org.spring.security.demo.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String messageKey;
    private final HttpStatus status;// key in messages.properties

    public BusinessException(String messageKey,HttpStatus status) {
        super(messageKey);
        this.messageKey = messageKey;
        this.status = status;
    }

    public BusinessException(String messageKey, HttpStatus status, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
        this.status = status;
    }
}

