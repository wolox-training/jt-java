package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	@Query("SELECT u FROM User u "
			+ "WHERE (:charSequence = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :charSequence, '%'))) "
			+ "AND (CAST(:begin as date) IS NULL OR u.birthdate >= :begin) "
			+ "AND (CAST(:end as date) IS NULL OR u.birthdate <= :end)")
	Page<User> findAllBirthdateBetweenAndNameLike(
			@Param("charSequence") String sequence,
			@Param("begin") LocalDate begin,
			@Param("end") LocalDate end,
			Pageable page);
}
