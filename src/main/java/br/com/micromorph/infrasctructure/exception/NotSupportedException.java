package br.com.micromorph.infrasctructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
public class NotSupportedException extends Throwable {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotSupportedException(final String message) {
        super(message);
    }
}
