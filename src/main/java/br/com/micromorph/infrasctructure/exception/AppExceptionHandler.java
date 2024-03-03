package br.com.micromorph.infrasctructure.exception;

import br.com.micromorph.application.resource.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;


@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request) {
		final ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MultipleResourceException.class)
	public ResponseEntity<?> multipleResourceException(final Exception ex, final WebRequest request) {
		final ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> constraintViolationException(final Exception ex, final WebRequest request) {
		final ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceSizeException.class)
	public ResponseEntity<?> resourceSizeException(final Exception ex, final WebRequest request) {
		final ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INSUFFICIENT_STORAGE);
	}

	@ExceptionHandler(PersistenceDeserializationException.class)
	public ResponseEntity<?> persistenceDeserializationException(final Exception ex, final WebRequest request) {
		final Response response = new Response(HttpStatus.CREATED.value(), ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@ExceptionHandler(NotSupportedException.class)
	public ResponseEntity<?> notSupportedException(final Exception ex, final WebRequest request) {
		final Response response = new Response(HttpStatus.NOT_IMPLEMENTED.value(), ex.getCause().getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED);
	}
}
