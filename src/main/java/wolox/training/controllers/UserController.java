package wolox.training.controllers;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	@ApiResponses({
			@ApiResponse(code = 200, message = SwaggerConstants.OK),
			@ApiResponse(code = 401, message = SwaggerConstants.UNAUTHORIZED)
	})
	public ResponseEntity<Page<User>> getAll(
			@ApiParam(value = "Index where paging is going to start", type = "query", required = true, name = "from", example = "0")
			@RequestParam(defaultValue = "0") Integer from,
			@ApiParam(value = "Number of books the page is containing", type = "query", required = true, name = "size", example = "10")
			@RequestParam(defaultValue = "10") Integer size,
			@ApiParam(value = "Name of the attribute the sorting is applying on", type = "query", required = true, name = "sort", example = "id")
			@RequestParam(defaultValue = "id") String sort) {
		return new ResponseEntity<>(userRepository.findAll(PageRequest.of(from, size, Sort.by(sort))), HttpStatus.OK);
	}

	/**
	 * Return a list of {@link User} that matches the provided parameters
	 * @param charSequence: Sequence of characters to search using a Regexp
	 * @param begin: Beginning date to search in a range of dates
	 * @param end: Ending date to search in a range of dates
	 * @return List of {@link User}
	 */
	@GetMapping("search")
	@ApiOperation(value = "Returns a list of all users that matches the search parameters", response = User.class, responseContainer = "List")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized")
	})
	public ResponseEntity<Page<User>> search(
			@ApiParam(value = "Sequence of characters", type = "query", required = true, name = "charSequence", example = "test")
			@RequestParam(required = false, defaultValue = "") String charSequence,
			@ApiParam(value = "Beginning date", type = "query", required = true, name = "begin", example = "2000-01-01")
			@RequestParam(required = false) String begin,
			@ApiParam(value = "Ending date", type = "query", required = true, name = "begin", example = "2000-01-01")
			@RequestParam(required = false) String end,
			@ApiParam(value = "Index where paging is going to start", type = "query", required = true, name = "from", example = "0")
			@RequestParam(defaultValue = "0") Integer from,
			@ApiParam(value = "Number of books the page is containing", type = "query", required = true, name = "size", example = "10")
			@RequestParam(defaultValue = "10") Integer size,
			@ApiParam(value = "Name of the attribute the sorting is applying on", type = "query", required = true, name = "sort", example = "id")
			@RequestParam(defaultValue = "id") String sort) {
		return new ResponseEntity<>(
				userRepository.findAllBirthdateBetweenAndNameLike(
						charSequence,
						!Strings.isNullOrEmpty(begin) ? LocalDate.parse(begin) : null,
						!Strings.isNullOrEmpty(begin) ? LocalDate.parse(end) : null,
						PageRequest.of(from, size, Sort.by(sort))), HttpStatus.OK);
	}

	/**
	 * Returns a specified {@link User} by the provided id
	 * @param id: Id of the {@link User}
	 * @return A {@link User}
	 * @throws UserNotFoundException: When the user id that was passed doesn't belong to any {@link User}
	 */
	@GetMapping("{id}")
	@ApiOperation(value = "Returns a specified user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.OK),
			@ApiResponse(code = 401, message = SwaggerConstants.UNAUTHORIZED),
			@ApiResponse(code = 404, message = SwaggerConstants.NOT_FOUND)
	})
	public ResponseEntity<User> get(
			@ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id) 
			throws UserNotFoundException {
		return new ResponseEntity<>(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(
				ExceptionsConstants.USER_NOT_FOUND)), HttpStatus.OK);
	}

	/**
	 * Return the {@link User} that's being authenticated
	 * @param authentication: {@link Authentication} credentials of the {@link User}
	 * @return Authenticated {@link User}
	 */
	@RequestMapping("me")
	@ApiOperation(value = "Returns current authenticated user", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SwaggerConstants.OK),
			@ApiResponse(code = 401, message = SwaggerConstants.UNAUTHORIZED),
			@ApiResponse(code = 404, message = SwaggerConstants.NOT_FOUND)
	})
	public ResponseEntity<User> getAuthenticatedUser(Authentication authentication)
			throws UserNotFoundException {
		return new ResponseEntity<>(userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UserNotFoundException(
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
			@ApiResponse(code = 401, message = SwaggerConstants.UNAUTHORIZED),
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
			@ApiResponse(code = 401, message = SwaggerConstants.UNAUTHORIZED),
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
			@ApiResponse(code = 401, message = SwaggerConstants.UNAUTHORIZED),
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
