package com.hhplus.reservation_concert.infrastructure.user;

import com.hhplus.reservation_concert.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryImpl extends JpaRepository<User, Long> {
}
