package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.DatabaseException;
import wolox.training.exceptions.IdMismatchException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping(value = "api/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Api
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * Returns a list of {@link Book}
     * @return List of {@link Book}
     */
    @GetMapping
    @ApiOperation(value = "Returns a list of all books", response = Book.class, responseContainer = "List")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<Book>> getAll() {
        return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
    }

    /**
     * Returns a specified book by the provided id
     * @param id: Id of the {@link Book}
     * @return A {@link Book}
     * @throws BookNotFoundException: When the book id that was passed doesn't belong to any {@link Book}
     */
    @GetMapping("{id}")
    @ApiOperation(value = "Returns a specified book", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<Book> get(@ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id) throws BookNotFoundException{
        return new ResponseEntity<>(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                ExceptionsConstants.BOOK_NOT_FOUND)), HttpStatus.OK);
    }

    /**
     * Creates a new {@link Book}
     * @param book: Data structure with the fields to create the {@link Book}
     * @return The created {@link Book} with the id assigned by the database
     * @throws DatabaseException: When the save method throws an error coming from the database
     */
    @PostMapping
    @ApiOperation(value = "Creates a book", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity<Book> create(@ApiParam(value = "user", type = "body", required = true, name = "body") @Valid @RequestBody Book book) throws DatabaseException {
        try {
            return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new DatabaseException(ExceptionsConstants.DATA_SAVE_INTEGRITY_VIOLATION);
        }
    }

    /**
     * Updates a specified {@link Book} receiving the new information in the same structure
     * @param id: Id of the {@link Book}
     * @param book: Data structure with the updated fields to update the {@link Book}
     * @return The updated {@link Book}
     * @throws BookNotFoundException: When the book id that was passed doesn't belong to any book
     * @throws IdMismatchException: When the book id that was passed doesn't match the id that's in within the JSON
     * @throws DatabaseException: When the save method throws an error coming from the database
     */
    @PutMapping("{id}")
    @ApiOperation(value = "Updates a specified book", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<Book> update(
            @ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id,
            @ApiParam(value = "user", type = "body", required = true, name = "body") @Valid @RequestBody Book book)
            throws BookNotFoundException, IdMismatchException, DatabaseException {
        if(book.getId() != id) {
            throw new IdMismatchException(ExceptionsConstants.ID_MISMATCH);
        }

        if(!bookRepository.existsById(id)) {
            throw new BookNotFoundException(ExceptionsConstants.BOOK_NOT_FOUND);
        }

        try {
            return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
        }  catch (Exception e) {
            throw new DatabaseException(ExceptionsConstants.DATA_SAVE_INTEGRITY_VIOLATION);
        }
    }

    /**
     * Deletes a specified {@link Book} by its id
     * @param id: Id of the {@link Book}
     * @return An empty, No Content response
     * @throws BookNotFoundException: When the book id that was passed doesn't belong to any book
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Deletes a specified user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<Void> delete(@ApiParam(value = "id", type = "path", required = true, name = "id", example = "1") @PathVariable int id) throws BookNotFoundException{
        if(!bookRepository.existsById(id)) {
            throw new BookNotFoundException(ExceptionsConstants.BOOK_NOT_FOUND);
        }
        bookRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
