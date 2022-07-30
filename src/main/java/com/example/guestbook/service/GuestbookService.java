package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.Guestbook;

// service 계즟에서는 DTO를 이용해 필요한 내용을 전달받고 반환하도록 처리한다.
public interface GuestbookService {
    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    // service 에서는 파라미터를 DTO타입으로 받기 때문에 JPA로 처리하기 위해서는 Entity 타입의 객체로 변환해야 하는 작업이 반드시 필요하다.
    // java 8 버전부터 인터페이스의 실제 내용을 가지는 코드는 default라는 키워드로 생성할 수 있다.
    // -> 실제 코드를 인터페이스에 선언할 수 있다.
    // -> 추상클래스를 생략하는 것이 가능해 졌다.
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
