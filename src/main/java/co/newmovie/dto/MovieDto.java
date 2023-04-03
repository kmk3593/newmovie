package co.newmovie.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import co.newmovie.entity.Movie;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieDto {

    private Long movieId;
    private String title;
    private LocalDateTime openingDate;
    private String zenre;
    private String thumbnail;
    private Integer totalSeat;

    private String movieDeleteYn;

    private Long ticketId;

    // AS
    private Double avgTicket;
    private Double avgScore;
    private Long userTicketId;
    private Long userScoreId;

    // @QueryProjection
    @Builder
    @QueryProjection
    public MovieDto(Long movieId, String title,
            LocalDateTime openingDate, String zenre, String thumbnail, Integer totalSeat,
            Double avgTicket, Double avgScore, Long userTicketId, Long userScoreId) {
        this.movieId = movieId;
        this.title = title;
        this.openingDate = openingDate;
        this.zenre = zenre;
        this.thumbnail = thumbnail;
        this.totalSeat = totalSeat;

        this.avgTicket = avgTicket;
        this.avgScore = avgScore;
        this.userTicketId = userTicketId;
        this.userScoreId = userScoreId;
    }

    public Movie toEntity() {
        return Movie.builder()
                // .movieId(this.movieId)
                .title(this.title)
                .openingDate(this.openingDate)
                .zenre(this.zenre)
                .thumbnail(this.thumbnail)
                .totalSeat(this.totalSeat)
                .movieDeleteYn("N")
                .build();
    }

}
