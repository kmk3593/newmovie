package co.newmovie.dto;

import java.time.LocalDateTime;

import co.newmovie.entity.Movie;
import lombok.Getter;

@Getter
public class MovieSelectDto {

    private Long movieId;
    private String title;
    private LocalDateTime openingDate;
    private String zenre;
    private Integer totalSeat;

    public MovieSelectDto(Movie entity) {
        this.movieId = entity.getMovieId();
        this.title = entity.getTitle();
        this.openingDate = entity.getOpeningDate();
        this.zenre = entity.getZenre();
        this.totalSeat = entity.getTotalSeat();
    }

}
