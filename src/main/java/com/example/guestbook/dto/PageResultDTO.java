package com.example.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// 화면에서 필요한 결과는 PageResultDTO 라는 이름으로 생성한다.
@Data
// 다양한 곳에서 사용할 수 있도록 제네릭 타입을 이용해 DTO와 EN(entity) 이라는 타입을 지정한다.
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList;

    // Function<EN, DTO> : entity 객체들을 DTO로 변환해 주는 기능
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());
    }
}
