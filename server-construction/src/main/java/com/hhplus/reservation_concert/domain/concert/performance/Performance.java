package com.hhplus.reservation_concert.domain.concert.performance;

import com.hhplus.reservation_concert.domain.concert.Concert;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(indexes = @Index(name = "idx_concert_id", columnList = "concert_id"))
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Concert concert;

    private LocalDateTime date;

    private String place;
}
