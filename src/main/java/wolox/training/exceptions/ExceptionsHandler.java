package wolox.training.exceptions;

import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler({
			BookNotFoundException.class,
			UserNotFoundException.class
	})
	public ResponseEntity<?> BookNotFoundHandler(Exception e) {
		ExceptionsResponse response = new ExceptionsResponse(e.getMessage());

		if (e instanceof BookNotFoundException) {
			response.setOrigin("/books");
		} else if (e instanceof UserNotFoundException) {
			response.setOrigin("/users");
		}
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({
			IdMismatchException.class,
			MethodArgumentNotValidException.class,
			DatabaseException.class,
			BookAlreadyOwnedException.class,
			ActionNotFoundException.class,
			MissingServletRequestParameterException.class
	})
	public ResponseEntity<?> DataIntegrityHandler(Exception e) {
		ExceptionsResponse response = new ExceptionsResponse(e.getMessage());

		if(e instanceof MethodArgumentNotValidException) {
			response.setError(Objects.requireNonNull(
					((MethodArgumentNotValidException) e).getBindingResult().getFieldError())
					.getDefaultMessage());
		}

		if(e instanceof MissingServletRequestParameterException) {
			response.setError(Objects.requireNonNull(
					e.getMessage()));
		}
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
