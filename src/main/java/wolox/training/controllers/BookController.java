package wolox.training.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import wolox.training.exceptions.IdMismatchException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Book> get(@PathVariable int id) throws BookNotFoundException{
        return new ResponseEntity<>(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(
                ExceptionsConstants.BOOK_NOT_FOUND)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Book> update(@PathVariable int id, @RequestBody Book book) throws BookNotFoundException, IdMismatchException {
        if(!bookRepository.existsById(id)) {
            throw new BookNotFoundException(ExceptionsConstants.BOOK_NOT_FOUND);
        }

        if(book.getId() != id) {
            throw new IdMismatchException(ExceptionsConstants.ID_MISMATCH_EXCEPTION);
        }
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) throws BookNotFoundException{
        if(!bookRepository.existsById(id)) {
            throw new BookNotFoundException(ExceptionsConstants.BOOK_NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
