package com.hhplus.reservation_concert.domain.concert;

import com.hhplus.reservation_concert.global.error.ErrorCode;
import com.hhplus.reservation_concert.global.error.exception.CustomException;
import com.hhplus.reservation_concert.infrastructure.concert.ConcertRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepositoryImpl concertRepository;

    public Concert getConcert(Long id) {
        return concertRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CONCERT_NOT_FOUND));
    }

    public List<Concert> getConcerts() {
        return concertRepository.findAll();
    }

    public void saveConcert(Concert concert) {
        concertRepository.save(concert);
    }
}
