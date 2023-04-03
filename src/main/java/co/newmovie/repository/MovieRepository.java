package co.newmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.newmovie.entity.Movie;

@Repository
public interface MovieRepository
        extends JpaRepository<Movie, Long>, MovieRepositoryCustom {
    // List<Movie> findByTitle(String title);

}
