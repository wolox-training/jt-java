package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.constants.ExceptionsConstants;
import wolox.training.constants.SwaggerConstants;
import wolox.training.enums.ActionsEnum;
import wolox.training.exceptions.ActionNotFoundException;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.DatabaseException;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping(value = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private PasswordEncoder encoder;

	/**
	 * Returns a list of {@link User}
	 * @return List of {@link User}
	 */
	@GetMapping
	@ApiOperation(value = "Returns a list of all users", response = User.class, responseContainer = "List")
	@ApiResponse(code = 200, message = SwaggerConstants.OK)
	public ResponseEntity<List<User>> getAll() {
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	/**
	 * Returns a specified book by the provided id
	 * @param id: Id of the {@link User}
	 * @return A {@link User}
	 * @throws UserNotFoundException: When the user id that was passed doesn't belong to any {@link User}
	 */
	@GetMapping("{id}")
	@ApiOperation(value = "Returns a specified user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.OK),
			@ApiResponse(code = 404, message = SwaggerConstants.NOT_FOUND)
	})
	public ResponseEntity<User> get(
			@ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id) 
			throws UserNotFoundException {
		return new ResponseEntity<>(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
				ExceptionsConstants.USER_NOT_FOUND)), HttpStatus.OK);
	}

	/**
	 * Creates a new {@link User}
	 * @param user: Data structure with the fields to create the {@link User}
	 * @return The created {@link User} with the id assigned by the database
	 * @throws DatabaseException : When the save method throws an error coming from the database
	 */
	@PostMapping
	@ApiOperation(value = "Creates an user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = SwaggerConstants.CREATED),
			@ApiResponse(code = 400, message = SwaggerConstants.BAD_REQUEST)
	})
	public ResponseEntity<User> create(
			@ApiParam(value = "user", type = "body", required = true, name = "body") @Valid @RequestBody User user)
			throws DatabaseException {
		try {
			user.setPassword(encoder.encode(user.getPassword()));
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new DatabaseException(ExceptionsConstants.DATA_SAVE_INTEGRITY_VIOLATION);
		}
	}

	/**
	 * Updates a specified {@link User} receiving the new information in the same structure
	 * @param id: Id of the {@link User}
	 * @param user: Data structure with the updated fields to update the {@link User}
	 * @return The updated {@link User}
	 * @throws UserNotFoundException: When the user id that was passed doesn't belong to any {@link User}
	 * @throws IdMismatchException: When the id that was passed doesn't match the id that's in within the JSON
	 * @throws DatabaseException: When the save method throws an error coming from the database
	 */
	@PutMapping("{id}")
	@ApiOperation(value = "Updates a specified user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.OK),
			@ApiResponse(code = 400, message = SwaggerConstants.BAD_REQUEST),
			@ApiResponse(code = 404, message = SwaggerConstants.NOT_FOUND)
	})
	public ResponseEntity<User> update(
			@ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id,
			@ApiParam(value = "user", type = "body", required = true, name = "body") @Valid @RequestBody User user)
			throws UserNotFoundException, IdMismatchException, DatabaseException {
		if(user.getId() != id) {
			throw new IdMismatchException(ExceptionsConstants.ID_MISMATCH);
		}

		if(!userRepository.existsById(id)) {
			throw new UserNotFoundException(ExceptionsConstants.USER_NOT_FOUND);
		}

		try {
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
		}  catch (Exception e) {
			throw new DatabaseException(ExceptionsConstants.DATA_SAVE_INTEGRITY_VIOLATION);
		}
	}

	/**
	 * Deletes a specified {@link User} by its id
	 * @param id: Id of the user
	 * @return An empty, No Content response
	 * @throws UserNotFoundException: UserNotFoundException: When the user id that was passed doesn't belong to any {@link User}
	 */
	@DeleteMapping("{id}")
	@ApiOperation(value = "Deletes a specified user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = SwaggerConstants.NO_CONTENT),
			@ApiResponse(code = 404, message = SwaggerConstants.NOT_FOUND)
	})
	public ResponseEntity<Void> delete(
			@ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id)
			throws UserNotFoundException {
		if(!userRepository.existsById(id)) {
			throw new UserNotFoundException(ExceptionsConstants.USER_NOT_FOUND);
		}
		userRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Adds or removes a {@link Book} from the user's book list
	 * @param userId: Id of the {@link User}
	 * @param bookId: Id of the {@link Book}
	 * @param action: Action to be performed
	 * @return The updated {@link User}
	 * @throws UserNotFoundException: When the user id that was passed doesn't belong to any {@link User}
	 * @throws BookNotFoundException: When the book id that was passed doesn't belong to any {@link Book}
	 * @throws BookAlreadyOwnedException: When the specified {@link User} already owns the provided {@link Book}
	 * @throws ActionNotFoundException: When the provided action doesn't exist
	 * @throws DatabaseException: When the save method throws an error coming from the database
	 */
	@PutMapping("{userId}/books/{bookId}")
	@ApiOperation(value = "Adds or removes a book", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.OK),
			@ApiResponse(code = 400, message = SwaggerConstants.BAD_REQUEST),
			@ApiResponse(code = 404, message = SwaggerConstants.NOT_FOUND)
	})
	public ResponseEntity<User> modifyBookList(
			@ApiParam(value = "userId", type = "path", required = true, name = "userId", example = "1") @PathVariable int userId,
			@ApiParam(value = "bookId", type = "path", required = true, name = "bookId", example = "1") @PathVariable int bookId,
			@ApiParam(value = "action", type = "query", required = true, name = "action", example = "add")
			@NotNull(message = ExceptionsConstants.MODIFY_BOOK_LIST_PARAMETHER_NOT_PRESENT) @RequestParam String action)
			throws UserNotFoundException, BookNotFoundException, BookAlreadyOwnedException, ActionNotFoundException, DatabaseException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
				ExceptionsConstants.USER_NOT_FOUND));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(
				ExceptionsConstants.BOOK_NOT_FOUND));

		if(action.equalsIgnoreCase(ActionsEnum.ADD.name())) {
			user.addBook(book);
		} else if (action.equalsIgnoreCase(ActionsEnum.REMOVE.name())) {
			user.removeBook(book);
		} else throw new ActionNotFoundException(ExceptionsConstants.ACTION_NOT_FOUND);

		try {
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
		} catch (Exception e) {
			throw new DatabaseException(ExceptionsConstants.DATA_SAVE_INTEGRITY_VIOLATION);
		}
	}
}
