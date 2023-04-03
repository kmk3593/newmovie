package co.newmovie.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.newmovie.dto.MovieDto;
import co.newmovie.dto.MovieSearchCondition;
import co.newmovie.dto.MovieSelectDto;
import co.newmovie.dto.ScoreDto;
import co.newmovie.dto.TicketDto;
import co.newmovie.repository.MovieRepository;
import co.newmovie.service.MovieService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final MovieRepository movieRepository;

    @GetMapping("/connectTest")
    public String Test() {
        System.out.println(LocalDateTime.now());
        return "테스트";
    }

    // 영화 등록
    @PostMapping("/movie")
    public Long saveMovie(@RequestBody MovieDto movieDto) {
        return movieService.saveMovie(movieDto);
    }

    // 영화 수정
    @PutMapping("/movie/{id}")
    public Long movieUpdate(@PathVariable Long id, @RequestBody MovieDto movieDto) {
        return movieService.movieUpdate(id, movieDto);
    }

    // 영화 상세 조회
    @GetMapping("/movie/{id}")
    public MovieSelectDto selectMovie(@PathVariable Long id) {
        return movieService.selectMovie(id);
    }

    // 영화 데이터 삭제
    // @DeleteMapping("/movie/{id}")
    // public void deleteMovie(@PathVariable Long id) {
    // movieService.deleteMovie(id);
    // }

    // 영화 삭제
    @PutMapping("/movie/{id}/delete")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    // 영화 검색
    @GetMapping("/movie")
    public Page<MovieDto> searchMovie(MovieSearchCondition condition, Pageable pageable) {
        return movieRepository.searchPage(condition, pageable);
    }

    // 예매
    @PostMapping("/movie/{id}/ticket")
    public void saveTicket(@PathVariable Long id, @RequestBody TicketDto ticketDto) {
        movieService.saveTicket(id, ticketDto);
    }

    // 평점
    @PostMapping("/movie/{id}/score")
    public Long saveScore(@PathVariable Long id, @RequestBody ScoreDto scoreDto) {
        return movieService.saveScore(id, scoreDto);
    }

    // 영화 api
    @GetMapping("/movie/search")
    public String searchMovie(String query) {
        return movieService.searchMovie(query);
    }

}
