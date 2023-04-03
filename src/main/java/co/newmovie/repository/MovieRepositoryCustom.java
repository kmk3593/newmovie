package co.newmovie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.newmovie.dto.MovieDto;
import co.newmovie.dto.MovieSearchCondition;

public interface MovieRepositoryCustom {

    Page<MovieDto> searchPage(MovieSearchCondition condition, Pageable pageable);

}
