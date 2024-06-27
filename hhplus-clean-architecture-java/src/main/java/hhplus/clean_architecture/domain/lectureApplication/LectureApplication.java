package hhplus.clean_architecture.domain.lectureApplication;

import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LectureApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Lecture lecture;

    private LocalDateTime applyDate;

    public static LectureApplication from(User user, Lecture lecture) {
        return LectureApplication.builder()
                .user(user)
                .lecture(lecture)
                .applyDate(LocalDateTime.now())
                .build();
    }
}
