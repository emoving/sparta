package com.hhplus.reservation_concert.domain.concert.seat;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.concert.SeatRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepositoryImpl seatRepository;

    public Seat getSeat(Long id) {
        return seatRepository.findByIdWithPessimisticLock(id).orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));
    }

    public List<Seat> getSeats(Performance performance) {
        return seatRepository.findAllByPerformance(performance);
    }

    public void saveSeat(Seat seat) {
        seatRepository.save(seat);
    }
}
