package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	Optional<Book> findTopByAuthor(String author);

	Optional<Book> findByIsbn(String isbn);

	@Query("SELECT b FROM Book b WHERE publisher = :publisher and b.year = :year and (b.genre = :genre OR :genre IS NULL)")
	List<Book> findAllByPublisherAndYearAndGenre(@Param("publisher") String publisher, @Param("year") String year, @Param("genre") String genre);
}
