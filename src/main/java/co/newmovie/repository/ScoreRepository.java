package co.newmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.newmovie.entity.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
