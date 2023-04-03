package co.newmovie.repository;

import javax.persistence.EntityManager;

import static co.newmovie.entity.QMovie.movie;
import static co.newmovie.entity.QTicket.ticket;
import static co.newmovie.entity.QScore.score;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.springframework.util.StringUtils.isEmpty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.newmovie.dto.MovieDto;
import co.newmovie.dto.MovieSearchCondition;
// import co.newmovie.dto.QMovieDto;
import co.newmovie.entity.QMovie;

public class MovieRepositoryImpl implements MovieRepositoryCustom {

        private final JPAQueryFactory queryFactory;

        private MovieRepositoryImpl(EntityManager em) {
                this.queryFactory = new JPAQueryFactory(em);
        }

        private BooleanExpression titleEq(String title) {
                return isEmpty(title) ? null : movie.title.contains(title);
        }

        private BooleanExpression zenreEq(String zenre) {
                return isEmpty(zenre) ? null : movie.zenre.contains(zenre);
        }

        // private BooleanExpression openGoeq(LocalDateTime openGoe) {
        // return openGoe == null ? null : movie.openingDate.goe(openGoe);
        // }

        // private BooleanExpression openLoeq(LocalDateTime openLoe) {
        // return openLoe == null ? null : movie.openingDate.loe(openLoe);
        // }

        private BooleanExpression openGoeq(String openGoe) {
                if (openGoe.equals("")) {
                        return null;
                } else {
                        openGoe = openGoe + " 00:00:00.000";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                        LocalDateTime dataTime = LocalDateTime.parse(openGoe, formatter);
                        System.out.println(dataTime);
                        return dataTime == null ? null : movie.openingDate.goe(dataTime);
                }
        }

        private BooleanExpression openLoeq(String openLoe) {
                if (openLoe.equals("")) {
                        return null;
                } else {
                        openLoe = openLoe + " 00:00:00.000";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                        LocalDateTime dataTime = LocalDateTime.parse(openLoe, formatter);
                        System.out.println(dataTime);
                        return dataTime == null ? null : movie.openingDate.loe(dataTime);
                }
        }

        private BooleanExpression openSetting(String openSet) {
                if (openSet.equals("BEFORE")) {
                        return movie.openingDate.loe(LocalDateTime.now());
                } else if (openSet.equals("AFTER")) {
                        return movie.openingDate.goe(LocalDateTime.now());
                } else if (openSet.equals("ALL")) {
                        return null;
                } else {
                        return null;
                }
        }

        StringPath aliasTicket = Expressions.stringPath("avgTicket");
        StringPath aliasScore = Expressions.stringPath("avgScore");

        private JPAQuery<MovieDto> getQeury() {
                QMovie m1 = new QMovie("m1");

                return queryFactory
                                .select(Projections.fields(MovieDto.class,
                                                movie.movieId,
                                                movie.title,
                                                movie.openingDate,
                                                movie.zenre,
                                                movie.thumbnail,
                                                movie.totalSeat,
                                                ticket.ticketId.count().divide(movie.totalSeat).multiply(100)
                                                                .doubleValue()
                                                                .as("avgTicket"),
                                                score.point.avg().as("avgScore"),
                                                ExpressionUtils.as(
                                                                JPAExpressions.select(ticket.ticketId)
                                                                                .from(m1)
                                                                                .leftJoin(ticket)
                                                                                .on(ticket.movieId.eq(m1.movieId))
                                                                                .where(movie.movieId.eq(m1.movieId),
                                                                                                ticket.userId.eq(1L))

                                                                                .groupBy(m1.movieId),
                                                                "userTicketId"),
                                                ExpressionUtils.as(
                                                                JPAExpressions.select(score.scoreId)
                                                                                .from(m1)
                                                                                .leftJoin(score)
                                                                                .on(score.movieId.eq(m1.movieId))
                                                                                .where(movie.movieId.eq(m1.movieId),
                                                                                                score.userId.eq(1L))
                                                                                .groupBy(m1.movieId),
                                                                "userScoreId")))
                                .from(movie)
                                .leftJoin(ticket).on(ticket.movieId.eq(movie.movieId))
                                .leftJoin(score).on(score.movieId.eq(movie.movieId));
        }

        @Override
        public Page<MovieDto> searchPage(MovieSearchCondition condition, Pageable pageable) {

                JPAQuery<MovieDto> results = getQeury()
                                .where(titleEq(condition.getTitle()),
                                                zenreEq(condition.getZenre()),
                                                openSetting(condition.getOpenSet()), // 전체, 상영전, 상영후
                                                openGoeq(condition.getOpenGoe()), // 개봉일 to ~
                                                openLoeq(condition.getOpenLoe()), // 개봉일 ~ from
                                                movie.movieDeleteYn.eq("N"));
                // openGoe , openLoe
                if (Sort.unsorted() != pageable.getSort()) {

                        // 정렬
                        results = results.orderBy(getOrderBy(pageable));

                }

                results = results.groupBy(movie.movieId)
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize());

                QueryResults<MovieDto> content = results.fetchResults();
                Long total = content.getTotal();
                return new PageImpl<>(content.getResults(), pageable, total);
        }

        private OrderSpecifier<?> getOrderBy(Pageable pageable) {

                if (!pageable.getSort().isEmpty()) {
                        // 정렬값이 들어 있으면 for 사용하여 값을 가져온다
                        for (Sort.Order order : pageable.getSort()) {
                                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                                Order direction = order.getDirection().isAscending() ? Order.DESC : Order.ASC; // DESC,
                                                                                                               // ASC 위치
                                                                                                               // 교환했다.
                                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                                switch (order.getProperty()) {
                                        case "OPENING_DATE":
                                                return new OrderSpecifier(direction, movie.openingDate);
                                        case "AVG_TICKET":
                                                return new OrderSpecifier(direction, aliasTicket);
                                        case "AVG_SCORE":
                                                return new OrderSpecifier(direction, aliasScore);
                                }
                        }
                }
                return null;
        }

        // @Override
        // public Page<MovieDto> searchPage(MovieSearchCondition condition, Pageable
        // pageable) {

        // QueryResults<MovieDto> results = queryFactory
        // .select(new QMovieDto(
        // movie.movieId,
        // movie.title,
        // movie.openingDate,
        // movie.zenre,
        // movie.thumbnail,
        // movie.totalSeat))
        // .from(movie)
        // .leftJoin(ticket).on(ticket.movieId.eq(1L))
        // .where(titleEq(condition.getTitle()),
        // zenreEq(condition.getZenre()))
        // .offset(pageable.getOffset())
        // .limit(pageable.getPageSize())
        // .fetchResults();

        // ----
        // QueryResults<MovieDto> results = queryFactory
        // .select(Projections.fields(MovieDto.class, movie.movieId, ticket.ticketId))
        // .from(movie)
        // .leftJoin(ticket).on(ticket.movieId.eq(movie.movieId))
        // .fetchResults();

        // ---

        // QMovie m1 = new QMovie("m1");

        // QueryResults<MovieDto> results = queryFactory
        // .select(new QMovieDto(
        // movie.movieId,
        // movie.title,
        // movie.openingDate,
        // movie.zenre,
        // movie.thumbnail,
        // movie.totalSeat,

        // ticket.ticketId.count().divide(movie.totalSeat).doubleValue().as("avgTicket"),
        // score.point.avg().as("avgScore"),
        // ExpressionUtils.as(
        // JPAExpressions.select(ticket.ticketId)
        // .from(m1)
        // .leftJoin(ticket).on(ticket.movieId.eq(m1.movieId))
        // .where(movie.movieId.eq(m1.movieId))
        // .groupBy(m1.movieId),
        // "userTicketId"),
        // ExpressionUtils.as(
        // JPAExpressions.select(score.scoreId)
        // .from(m1)
        // .leftJoin(score).on(score.movieId.eq(m1.movieId))
        // .where(movie.movieId.eq(m1.movieId))
        // .groupBy(m1.movieId),
        // "userScoreId")))
        // .from(movie)
        // .leftJoin(ticket).on(ticket.movieId.eq(movie.movieId))
        // .leftJoin(score).on(score.movieId.eq(movie.movieId))
        // .where(titleEq(condition.getTitle()),
        // zenreEq(condition.getZenre()))
        // .groupBy(movie.movieId)
        // .offset(pageable.getOffset())
        // .limit(pageable.getPageSize())
        // .fetchResults();

        // List<MovieDto> content = results.getResults();
        // Long total = results.getTotal();
        // return new PageImpl<>(content, pageable, total);
        // }

}
