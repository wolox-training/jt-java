package wolox.training.exceptions;

import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<?> BookNotFoundHandler(Exception e) {
		ExceptionsResponse response = new ExceptionsResponse("/books", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({
			IdMismatchException.class,
			DataIntegrityViolationException.class,
			MethodArgumentNotValidException.class,
	})
	public ResponseEntity<?> DataIntegrityHandler(Exception e) {
		ExceptionsResponse response = new ExceptionsResponse(e.getMessage());

		if(e instanceof IdMismatchException) {
			response.setOrigin("/books");
		}
		if(e instanceof MethodArgumentNotValidException) {
			response.setError(Objects.requireNonNull(((MethodArgumentNotValidException) e).getBindingResult().getFieldError()).getDefaultMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
