package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.entity.QGuestbook;
import com.example.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor  // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {

        //service interface 에 구현된 dtoToEntity 활용
        Guestbook entity = dtoToEntity(dto);
        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        // 검색 조건 처리
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    // Querydsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword();

        // 검색 조건이 없다면 gno > 0 으로만 생성된다.
        BooleanExpression expression = qGuestbook.gno.gt(0L);

        booleanBuilder.and(expression);

        // 검색 조건이 없는 경우
        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        // 검색 조건 작성
        // PageRequestDTO 를 파라미터로 받아 검색조건이 있는 경우 conditionBuilder 변수를 생성해 각 검색조건을 or로 연결해 처리한다.
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 결합
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        // findById() 를 통해 Entity 객체를 가져왔다면 entityToDto()를 이용해 Entity 객체를 DTO로 변환해 반환한다.
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }
}
