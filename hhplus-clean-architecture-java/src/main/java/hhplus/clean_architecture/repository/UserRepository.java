package hhplus.clean_architecture.repository;

import hhplus.clean_architecture.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
