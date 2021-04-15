package org.dongho.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.dongho.guestbook.entity.Guestbook;
import org.dongho.guestbook.entity.QGuestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i -> {
         Guestbook guestbook = Guestbook.builder().title("title"+i)
                               .content("content"+i)
                               .writer("user" + (i % 10))
                               .build();
            // 서버시간을 기준으로 save된다
            // 리턴 타입은 guestbook 객체
            log.info(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void testUpdate(){
        Guestbook guestbook = guestbookRepository.findById(300L).get();

        log.info("BEFORE-------------------");
        log.info(guestbook);

        guestbook.changeTitle("Update 300 Title");
        guestbook.changeContent("Update 300 Content");

        log.info("AFTER-----------------------");
        log.info(guestbook);

        // 현재는 업데이트 하기전에 select 하고 나서 update 한다
        // @query를 이용해서 처리하면 select 다시 안날릴 수 있다
        guestbookRepository.save(guestbook);
    }

    @Test
    public void testQuery1(){

        Pageable pageable =
                PageRequest.of(0,10, Sort.by("gno").descending());
        // 검색조건 만들기
        // guestbook 얻어오기
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";
        // true , false 조건 만들기
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // title like (keyword) 와 같다
        BooleanExpression expression =
                qGuestbook.title.contains(keyword);
        // 검색 조건문을 booleanBuilder에 추가해서 사용
        booleanBuilder.and(expression);
        // booleanBuilder 이용해서 실행 -> 검색 + 페이징
        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder , pageable);

        result.get().forEach(guestbook -> {
            log.info(guestbook);
        });
    }
    // 검색 조건을 여러개 주고 화면에서 t,w,c 등으로 입력받는 것까지 처리
    @Test
    public void testSearch(){
        // 페이지 조건
        Pageable pageable =
                PageRequest.of(0,10, Sort.by("gno").descending());

        String keyword = "1";

        String[] arr = {"t","w"};

        // Qdomain 처리 , 필드값으로 사용
        QGuestbook qGuestbook = QGuestbook.guestbook;
        // total 조건
        BooleanBuilder total = new BooleanBuilder();

        // gno 조건
        BooleanBuilder gno = new BooleanBuilder();

        // condition (expressions 생성)
        BooleanBuilder condition = new BooleanBuilder();

        if(arr != null && arr.length > 0) {
            Arrays.stream(arr).forEach(type -> {
                log.info("type: " + type);
                switch (type) {
                    case "t":
                        condition.or(qGuestbook.title.contains(keyword));
                        break;
                    case "c":
                        condition.or(qGuestbook.content.contains(keyword));
                        break;
                    case "w":
                        condition.or(qGuestbook.writer.contains(keyword));
                }
            }); // loop 처리
        }
        total.and(condition);
        // gno는 0보다 크다는 조건
        gno.and(qGuestbook.gno.gt(0L));

        total.and(gno);

        guestbookRepository.findAll(total , pageable);
    }
}
