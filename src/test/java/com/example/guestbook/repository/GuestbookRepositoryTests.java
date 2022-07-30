package com.example.guestbook.repository;

import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    // 수정 시간 테스트
    @Test
    public void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(200L);

        if (result.isPresent()) {
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }
    }

    // Querydsl 테스트 - 단일항목테스트(title 에 1이 포함된 내용 출력)
    @Test
    public void testQuery1() {

        // 페이징 처리
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // Predicate
        // Q도메인 클래스를 사용하면 엔티티 클래스에 선언된 필드를 변수로 사용할 수 있다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        // BooleanBuilder : 쿼리의 where문에 들어가는 조건을 넣어주는 컨테이너
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // title like %1% 쿼리를 코드로 구현
        BooleanExpression expression = qGuestbook.title.contains("1");

        // 만들어진 조건 결합
        booleanBuilder.and(expression);

        // BooleanBuilder 는 guestbookRepository에 추가된 QuerydslPredicateExecutor 인터페이스의 findAll()을 사용할 수 있다.
        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    // Querydal 테스트 - 다중항목테스트 (title 이나 content 에 1이 포함된 내용 출력)
    @Test
    public void testQuery2() {

        // 페이징 처리
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //title like %1% or content like %1% 쿼리를 코드로 구현
        BooleanExpression expression = qGuestbook.title.contains("1");
        BooleanExpression exAll = expression.or(qGuestbook.content.contains("1"));

        // 만들어진 조건 결합
        booleanBuilder.and(exAll);
        booleanBuilder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
