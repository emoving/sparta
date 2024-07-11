package com.hhplus.reservation_concert.domain.concert.seat;

import com.hhplus.reservation_concert.domain.concert.performance.Performance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Performance performance;

    private Integer number;

    private Integer price;
}
