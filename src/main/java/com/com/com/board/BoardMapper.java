package com.com.com.board;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BoardMapper {
	public List<BoardVO> getList();
	public List<BoardVO> getList2();
	public void insertBoard(BoardVO boardVO);
	public BoardVO getBoard(int seq);
	public void update(BoardVO boardVO);
	public void delete(int seq);
	public void updateViewCnt(int seq);
	// 게시물 총 갯수
	public int countBoard();
	public int countSearched(@Param("type")String type, @Param("keyword")String keyword);
	// 페이징 처리 게시글 조회
	public List<BoardVO> selectBoard(PagingVO vo);
	public List<BoardVO> selectSearch(@Param("vo") PagingVO vo, @Param("type")String type, @Param("keyword")String keyword);
	public int selectSearchCount(@Param("type")String type,@Param("keyword")String keyword);
	public List<BoardVO> searchByDate(@Param("vo") PagingVO vo, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
	public int countSearchedByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	public void insertFile(FileVO fileVO);
	public List<FileVO> getFile(int seq);
	public void deleteFilesByListSeq(int listSeq);
	public List<BoardVO> searchBoard(@Param("type") String type, @Param("keyword") String keyword);
	
}



