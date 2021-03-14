package com.example.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@Data 어노테이션은 @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode를
//한꺼번에 설정해준다. ServiceTests.testList()에서 totalPage등의 변수에 대한
// get, set, isXXX(불린타입인 경우)의 메소드들을 생성해주게 된다.
// https://www.daleseo.com/lombok-popular-annotations/
@Data
//DTO와 EN 이라는 타입을 받을 수 있도록 제네릭으로 선언한다.
public class PageResultDTO<DTO, EN> {
  private List<DTO> dtoList;
  //Page는 import 해오는 객체의 타입이며, 여기에는 Page 객체에 어떤 타입을 받을지를 <EN>으로 설정하여
  //말 그대로의 Entity 타입을 담는 Page 객체를 result로 설정한다. 이렇게 해서 어떤 타입의 Page<E>이든
  //받을 수 있는 구조가 되는 것이다.
  //Function 함수적 인터페이스는 Function<T, R>의 경우 applyXXX() 라는 추상 메서드를 통해
  // T 타입을 R 타입으로 변경하여 매핑해준다.
  //여기서의 Function 인터페이스는 <EN> 타입들로 구성된 Page 내부의 객체들을 DTO 타입으로 변경해준다.
  // 예시코드 : https://cornswrold.tistory.com/309

  //총 페이지 번호
  private int totalPage;
  //현재 페이지 번호
  private int page;
  //목록 사이즈
  private int size;
  //시작 페이지 번호, 끝 페이지 번호
  private int start, end;
  //이전, 다음
  private boolean prev, next;
  //페이지 번호 목록
  private List<Integer> pageList;

  public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
    dtoList = result.stream().map(fn).collect(Collectors.toList());
    totalPage = result.getTotalPages();
    makePageList(result.getPageable());
  }

  private void makePageList(Pageable pageable){
    this.page = pageable.getPageNumber() + 1; //0부터 시작하므로 1추가
    this.size = pageable.getPageSize();

    //temp end page
    int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
    start = tempEnd - 9;
    prev = start > 1;
    end = totalPage > tempEnd ? tempEnd: totalPage;
    next = totalPage > tempEnd;
    pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
  }

}
