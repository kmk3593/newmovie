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
public class Ticket {

    @Id
    @GeneratedValue
    private Long ticketId;
    private LocalDateTime ticketDate;
    private Long movieId;
    private Long userId;

    // public Ticket(Date ticketDate, Long movieId, Long userId) {

    // this.ticketDate = ticketDate;
    // this.movieId = movieId;
    // this.userId = userId;

    // }

}
