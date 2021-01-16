package wolox.training.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	Optional<Book> findTopByAuthor(String author);

	Optional<Book> findByIsbn(String isbn);

	@Query("SELECT b FROM Book b "
			+ "WHERE (b.publisher = :publisher OR :publisher IS NULL) "
			+ "AND (b.year = :year OR :year IS NULL) "
			+ "AND (b.genre = :genre OR :genre IS NULL)")
	Page<Book> findAllByPublisherAndYearAndGenre(
			@Param("publisher") String publisher,
			@Param("year") String year,
			@Param("genre") String genre,
			Pageable page);

	@Query("SELECT b FROM Book b " +
			"WHERE (:genre IS NULL OR b.genre = :genre) " +
			"AND (:author IS NULL OR b.author = :author) " +
			"AND (:image IS NULL OR b.image = :image) " +
			"AND (:title IS NULL OR b.title = :title) " +
			"AND (:subtitle IS NULL OR b.subtitle = :subtitle) " +
			"AND (:publisher IS NULL OR b.publisher = :publisher) " +
			"AND (:year IS NULL OR b.year = :year) " +
			"AND (:pages <= 0 or b.pages = :pages) " +
			"AND (:isbn IS NULL OR b.isbn = :isbn)")
	Page<Book> findAll(
			@Param("genre") String genre,
			@Param("author") String author,
			@Param("image") String image,
			@Param("title") String title,
			@Param("subtitle") String subtitle,
			@Param("publisher") String publisher,
			@Param("year") String year,
			@Param("pages") int pages,
			@Param("isbn") String isbn,
			Pageable page);
}
