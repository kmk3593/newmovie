package co.newmovie.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import co.newmovie.entity.Score;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreDto {

    private Long scoreId;
    private Integer point;
    private String detail;
    private LocalDateTime scoreDate;

    private Long movieId;
    private Long userId;

    @Builder
    @QueryProjection
    public ScoreDto(Long scoreId, Integer point, String detail, LocalDateTime scoreDate, Long movieId, Long userId) {
        this.scoreId = scoreId;
        this.point = point;
        this.detail = detail;
        this.scoreDate = scoreDate;
        this.movieId = movieId;
        this.userId = userId;
    }

    public Score toEntity(Long movieId) {
        return Score.builder()
                // .scoreId(this.scoreId)
                .point(this.point)
                .detail(this.detail)
                .scoreDate(LocalDateTime.now())
                .movieId(movieId)
                .userId(this.userId)
                .build();
    }

}
