package com.hhplus.reservation_concert.infrastructure.user;

import com.hhplus.reservation_concert.domain.user.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepositoryImpl extends JpaRepository<PointHistory, Long> {
}
