package com.example.guestbook;

import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.repository.GuestbookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class GuestbookApplicationTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    // 더미 데이터 생성
    @Test
    public void insertDDumies(){

        IntStream.rangeClosed(1,300).forEach(i ->{
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer("user" + (i % 10))
                    .build();
            guestbookRepository.save(guestbook);
        });
    }
}
