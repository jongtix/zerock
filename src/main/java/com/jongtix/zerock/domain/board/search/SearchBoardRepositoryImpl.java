package com.jongtix.zerock.domain.board.search;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.QBoard;
import com.jongtix.zerock.domain.board.QMember;
import com.jongtix.zerock.domain.board.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {

        log.info("search1..........");

        QMember member = QMember.member;
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);

        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
//        jpqlQuery.select(board).where(board.bno.eq(1L));
//        jpqlQuery.select(board, member.email, reply.count()).groupBy(board);
//
//        log.info("----------------------");
//        log.info(jpqlQuery);
//        log.info("----------------------");

//        List<Board> result = jpqlQuery.fetch();

        //Tuple 사용하도록 변경
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count()).groupBy(board);

        log.info("----------------------");
        log.info(tuple);
        log.info("----------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @SuppressWarnings("unchecked")  //Type Safety : unchecked 문제 무시
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {  //Service 단에서 DTO를 분리하여 Repository 영역에 전달하는 것이 좋음

        log.info("searchPage............");

        QMember member = QMember.member;
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if (type != null) {
            String[] typeArr = type.split("");
            BooleanBuilder typeBooleanBuilder = new BooleanBuilder();

            for (String condition : typeArr) {
                switch (condition) {
                    case "t":
                        typeBooleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        typeBooleanBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        typeBooleanBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(typeBooleanBuilder);
        }

        tuple.where(booleanBuilder);

        tuple.groupBy(board);

        //sort 처리 추가
        Sort sort = pageable.getSort();

        sort.stream().forEach(
                order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String prop = order.getProperty();

                    PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
                    tuple.orderBy(new OrderSpecifier<>(direction, orderByExpression.get(prop)));
                }
        );

        //page 처리 추가
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        log.info(result);
        log.info(count);

        return new PageImpl<Object[]>(
                result.stream()
                        .map(row -> row.toArray())
                        .collect(Collectors.toList()),
                pageable,
                count
        );
    }

}
