package co.newmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.newmovie.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
