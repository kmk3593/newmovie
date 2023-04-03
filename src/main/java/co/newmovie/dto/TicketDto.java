package co.newmovie.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import co.newmovie.entity.Movie;
import co.newmovie.entity.Ticket;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketDto {

    private Long ticketId;
    private LocalDateTime ticketDate;
    private Long movieId;
    private Long userId;

    @Builder
    @QueryProjection
    public TicketDto(Long ticketId, LocalDateTime ticketDate, Long movieId, Long userId) {
        this.ticketId = ticketId;
        this.ticketDate = ticketDate;
        this.movieId = movieId;
        this.userId = userId;
    }

    public Ticket toEntity(Long movieId) {
        return Ticket.builder()
                // .ticketId(this.ticketId)
                .ticketDate(LocalDateTime.now())
                .movieId(movieId)
                .userId(this.userId)
                .build();
    }

}
