package hhplus.clean_architecture.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    USER_NOT_FOUND(404, "해당 유저가 없습니다."),
    USER_ALREADY_APPLY_LECTURE(403, "이미 수강중인 특강입니다."),
    USER_NOT_APPLY_LECTURE(404, "특강 등록자 명단에 없습니다."),

    LECTURE_NOT_FOUND(404, "해당 특강이 없습니다."),
    LECTURE_CAPACITY_FULL(409, "해당 특강의 정원이 가득찼습니다."),

    LECTURE_APPLICATION_NOT_FOUND(404, "해당 신청서가 없습니다.");

    private final int status;
    private final String message;
}
