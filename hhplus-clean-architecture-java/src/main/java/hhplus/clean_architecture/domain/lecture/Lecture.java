package hhplus.clean_architecture.domain.lecture;

import hhplus.clean_architecture.domain.lectureApplication.LectureApplication;
import hhplus.clean_architecture.domain.user.User;
import hhplus.clean_architecture.global.BaseException;
import hhplus.clean_architecture.global.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Long currentApplications;
    private Long maxApplications;
    private LocalDateTime date;
    private LocalDateTime applyStartDate;

    @OneToMany(mappedBy = "lecture")
    private List<LectureApplication> lectureApplications = new ArrayList<>();

    public boolean hasApplied(User user) {
        return lectureApplications.stream()
                .anyMatch(application -> application.getUser().equals(user));
    }

    public void apply(User user) {
        if (this.currentApplications >= this.maxApplications) {
            throw new BaseException(ErrorCode.LECTURE_CAPACITY_FULL);
        }

        if (hasApplied(user)) {
            throw new BaseException(ErrorCode.USER_ALREADY_APPLY_LECTURE);
        }

        this.currentApplications++;
    }
}
