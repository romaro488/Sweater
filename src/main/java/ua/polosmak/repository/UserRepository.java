package ua.polosmak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.polosmak.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
