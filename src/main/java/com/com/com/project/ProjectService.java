package com.com.com.project;

import java.util.List;

public interface ProjectService {
    public boolean login(String id, int password);
    public void write(EPBoardVO epboardVO);
    public int getNextSeq();
    public List<EPBoardVO> getAllWrittenContent();
    public List<EPBoardVO> getWrittenContentByUser(String name);
    public EPBoardVO getBoard(int seq);
    public void updateBoard(EPBoardVO epboardVO, String C_name); // 업데이트 메서드 추가
    public List<EPBoardVO> getAllVisibleContent(String userName);
	void insertHistory(EPHistoryVO historyVO);
	List<EPHistoryVO> getHistoryBySeq(int seq);
	
}
