package hhplus.clean_architecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hhplus.clean_architecture.domain.LectureFacade;
import hhplus.clean_architecture.domain.lecture.Lecture;
import hhplus.clean_architecture.domain.lectureApplication.LectureApplication;
import hhplus.clean_architecture.domain.user.User;
import hhplus.clean_architecture.global.BaseException;
import hhplus.clean_architecture.global.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hhplus.clean_architecture.controller.LectureDto.LectureRequest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LectureFacade lectureFacade;

    private User user;
    private Lecture lecture;
    private Lecture lecture2;

    @BeforeEach
    void setUp() {
        LocalDateTime lectureDate = LocalDateTime.of(2024, 6, 29, 17, 0, 0);
        LocalDateTime lectureApplyDate = LocalDateTime.of(2024, 6, 29, 0, 0, 0);

        user = new User(1L, "이동주");
        lecture = new Lecture(1L, "스프링", 0L, 30L, lectureDate, lectureApplyDate, new ArrayList<>());
        lecture2 = new Lecture(2L, "리액트", 0L, 50L, lectureDate, lectureApplyDate, new ArrayList<>());
    }

    @Test
    void apply() throws Exception {
        LectureApplication lectureApplication = LectureApplication.from(user, lecture);
        when(lectureFacade.apply(1L, 1L)).thenReturn(lectureApplication);
        LectureRequest lectureRequest = new LectureRequest(1L, 1L);

        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lectureRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("스프링"))
                .andExpect(jsonPath("$.currentApplications").value(0L))
                .andExpect(jsonPath("$.maxApplications").value(30L));
    }

    @Test
    void alreadyApply() throws Exception {
        LectureRequest lectureRequest = new LectureRequest(1L, 1L);
        doThrow(new BaseException(ErrorCode.USER_ALREADY_APPLY_LECTURE)).when(lectureFacade).apply(anyLong(), anyLong());

        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lectureRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("이미 수강중인 특강입니다."));
    }

    @Test
    void applyLectureFullError() throws Exception {
        LectureRequest lectureRequest = new LectureRequest(1L, 1L);
        doThrow(new BaseException(ErrorCode.LECTURE_CAPACITY_FULL)).when(lectureFacade).apply(anyLong(), anyLong());

        mockMvc.perform(post("/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lectureRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("해당 특강의 정원이 가득찼습니다."));
    }

    @Test
    void getLectures() throws Exception {
        List<Lecture> lectures = Arrays.asList(lecture, lecture2);
        when(lectureFacade.getLectures()).thenReturn(lectures);

        mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureResponses").isArray())
                .andExpect(jsonPath("$.lectureResponses.length()").value(2));
    }
}