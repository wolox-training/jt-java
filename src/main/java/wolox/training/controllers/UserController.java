package wolox.training.controllers;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;

	/**
	 * Returns a list of {@link User}
	 * @return List of {@link User}
	 */
	@GetMapping
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
	public ResponseEntity<User> get(@PathVariable int id) throws UserNotFoundException {
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
	public ResponseEntity<User> create(@Valid @RequestBody User user) throws DatabaseException {
		try {
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
	public ResponseEntity<User> update(@PathVariable int id,@Valid @RequestBody User user)
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
	public ResponseEntity<Void> delete(@PathVariable int id) throws UserNotFoundException {
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
	public ResponseEntity<User> modifyBookList(@PathVariable int userId, @PathVariable int bookId, @RequestParam String action)
			throws UserNotFoundException, BookNotFoundException, BookAlreadyOwnedException, ActionNotFoundException, DatabaseException {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
				ExceptionsConstants.USER_NOT_FOUND));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(
				ExceptionsConstants.BOOK_NOT_FOUND));

		if(action.equals("add")) {
			user.addBook(book);
		} else if (action.equals("remove")) {
			user.removeBook(book);
		} else throw new ActionNotFoundException(ExceptionsConstants.ACTION_NOT_FOUND);

		try {
			return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
		} catch (Exception e) {
			throw new DatabaseException(ExceptionsConstants.DATA_SAVE_INTEGRITY_VIOLATION);
		}
	}
}
