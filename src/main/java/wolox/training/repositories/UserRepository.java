package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);

	@Query("SELECT u from User u WHERE (LOWER(u.name) LIKE LOWER(CONCAT('%', :charSequence, '%'))) AND (u.birthdate >= :begin AND u.birthdate <= :end)")
	List<User> findAllBirthdateBetweenAndNameLike(@Param("charSequence") String sequence, @Param("begin") LocalDate begin, @Param("end") LocalDate end);
}
