package co.newmovie.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.newmovie.dto.MovieDto;
import co.newmovie.dto.MovieSelectDto;
import co.newmovie.dto.ScoreDto;
import co.newmovie.dto.TicketDto;
import co.newmovie.entity.Movie;
import co.newmovie.entity.Ticket;
import co.newmovie.repository.MovieRepository;
import co.newmovie.repository.ScoreRepository;
import co.newmovie.repository.TicketRepository;
import co.newmovie.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.nio.charset.Charset;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MovieService {

        private final MovieRepository movieRepository;
        private final TicketRepository ticketRepository;
        private final ScoreRepository scoreRepository;
        private final UserRepository userRepository;

        // 영화 등록
        @Transactional
        public Long saveMovie(MovieDto movieDto) {
                return movieRepository.save(movieDto.toEntity()).getMovieId();
        }

        // 영화 수정
        @Transactional
        public Long movieUpdate(Long id, MovieDto movieDto) {
                Movie movie = movieRepository.findById(id).orElseThrow(
                                () -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));

                movie.movieUpdate(movieDto.getTitle(), movieDto.getOpeningDate(), movieDto.getZenre(),
                                movieDto.getThumbnail(),
                                movieDto.getTotalSeat());

                return id;
        }

        // 영화 상세 조회
        public MovieSelectDto selectMovie(Long id) {
                Movie entity = movieRepository.findById(id).orElseThrow(
                                () -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));

                return new MovieSelectDto(entity);
        }

        // 영화 데이터 삭제
        // @Transactional
        // public void deleteMovie(Long id) {
        // Movie movie = movieRepository.findById(id).orElseThrow(
        // () -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));

        // movieRepository.deleteById(id);
        // }

        // 영화 삭제 - DeleteYn = "Y"
        @Transactional
        public void deleteMovie(Long id) {
                Movie movie = movieRepository.findById(id).orElseThrow(
                                () -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));

                movie.movieDelete();
        }

        // 영화 예매
        public void saveTicket(Long id, TicketDto ticketDto) {
                // 예매 Id, 예매한 시간, 영화 ID, 유저 ID
                Movie movie = movieRepository.findById(id).orElseThrow(
                                () -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));

                ticketRepository.save(ticketDto.toEntity(movie.getMovieId()));
        }

        // 평점 등록
        public Long saveScore(Long id, ScoreDto scoreDto) {
                // 평가 ID, 평점, 세부평가, 평가날짜, 영화ID, 유저ID
                Movie movie = movieRepository.findById(id).orElseThrow(
                                () -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));
                return scoreRepository.save(scoreDto.toEntity(movie.getMovieId())).getMovieId();
        }

        // 영화 API
        public String searchMovie(String query) {

                URI uri = UriComponentsBuilder
                                .fromUriString("https://openapi.naver.com")
                                .path("/v1/search/movie.json")
                                .queryParam("query",
                                                query)
                                .queryParam("display", 10)
                                .queryParam("start", 1)
                                .encode(Charset.forName("UTF-8"))
                                .encode()
                                .build()
                                .toUri();

                RequestEntity<Void> raq = (RequestEntity<Void>) RequestEntity
                                .get(uri)
                                .header("X-Naver-Client-Id", "0j2gVVO3vlupcamnyXyV")
                                .header("X-Naver-Client-Secret", "ZLEq0VuW8N")
                                .build();

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> result = restTemplate.exchange(raq, String.class);
                return result.getBody();
        }

}
