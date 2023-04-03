package co.newmovie.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long movieId;

    private String title;
    private LocalDateTime openingDate;
    private String zenre;
    private String thumbnail;
    private Integer totalSeat;

    private String movieDeleteYn;

    // public Movie(Long movieId) {
    // this.movieId = movieId;
    // }

    // UPDATE
    public void movieUpdate(String title, LocalDateTime openingDate, String zenre, String thumbnail,
            Integer totalSeat) {
        this.title = title;
        this.openingDate = openingDate;
        this.zenre = zenre;
        this.thumbnail = thumbnail;
        this.totalSeat = totalSeat;
    }

    // Delete
    public void movieDelete() {
        this.movieDeleteYn = "Y";
    }

}
