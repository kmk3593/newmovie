package co.newmovie.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MovieSearchCondition {

    private String title;
    private String zenre;

    // 전체, 상영 전, 상영 후 선택해서 조회
    private String openSet;

    // 달력 선택 - 개봉일
    // private LocalDateTime openGoe;
    // private LocalDateTime openLoe;

    private String openGoe;
    private String openLoe;
}
