package com.com.com.project;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProjectMapper {
    public MemberVO getMember(String id);
    public void insertBoard(EPBoardVO epboardVO); // 글쓰기 메서드 추가

    @Select("SELECT ep_board_seq.NEXTVAL FROM dual")
    public int getNextSeq(); // 시퀀스 번호 가져오기 메서드

    public List<EPBoardVO> getAllWrittenContent();
    public List<EPBoardVO> getWrittenContentByUser(String name);

    public EPBoardVO getBoard(int seq);
    public void updateBoard(EPBoardVO epboardVO); // 업데이트 메서드 추가

    @Select("SELECT * FROM ep_board WHERE name = #{name} AND wait != '임시저장'")
    public List<EPBoardVO> getVisibleContentByUser(String name); // 중복된 메서드 제거
    @Select("SELECT b.*, m.grade " +
            "FROM ep_board b " +
            "JOIN ep_member m ON b.name = m.name " +
            "WHERE (b.wait != '임시저장') OR (b.wait = '임시저장' AND b.name = #{userName})")
    List<EPBoardVO> getAllVisibleContent(String userName);
    // 히스토리 저장 메서드
    public void insertHistory(EPHistoryVO historyVO);

    // 히스토리 조회 메서드
    public List<EPHistoryVO> getHistoryBySeq(int seq);
    
    List<EPBoardVO> searchContent(@Param("searchType") String searchType, @Param("searchKeyword") String searchKeyword, @Param("approvalStatus") String approvalStatus, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("userName") String userName, @Param("grade") String grade);
    public MemberVO getMemberByName(String name);
    
}

