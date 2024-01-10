package br.com.micromorph.infrasctructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.CREATED)
public class PersistenceDeserializationException extends Exception{

    @Serial
    private static final long serialVersionUID = 1L;

    public PersistenceDeserializationException(final String message) {
        super(message);
    }
}
