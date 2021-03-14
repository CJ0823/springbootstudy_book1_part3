package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Member;

public interface BoardService {
  Long register(BoardDTO dto);

  PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); //목록처리

  BoardDTO get(Long bno);

  void modify(BoardDTO boardDTO);

  void removeWithReplies(Long bno); //삭제 기능

  default Board dtoToEntity(BoardDTO dto) {
    Member member = Member.builder()
            .email(dto.getWriterEmail()).build();
    Board board = Board.builder()
            .bno(dto.getBno())
            .title(dto.getTitle())
            .content(dto.getContent())
            .writer(member)
            .build();
    return board;
  }

  default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
    BoardDTO boardDTO = BoardDTO.builder()
            .bno(board.getBno())
            .title(board.getTitle())
            .content(board.getContent())
            .regDate(board.getRegDate())
            .modDate(board.getModDate())
            .writerEmail(member.getEmail())
            .writerName(member.getName())
            .replyCount(replyCount.intValue()) //long으로 나오므로 int로 변환
            .build();

    return boardDTO;
  }
}
