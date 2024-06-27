package com.com.com.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper mapper;

    public MemberVO getMember(String id) {
        return mapper.getMember(id);
    }

    @Override
    public boolean login(String id, int password) {
        MemberVO member = mapper.getMember(id);
        if (member != null && member.getPassword() == password) {
            return true;
        }
        return false;
    }

    @Override
    public void write(EPBoardVO epboardVO) {
    	 System.out.println("Writing EPBoardVO: " + epboardVO); // 디버깅 출력
//        epboardVO.setWait("결재대기"); // 글 작성 시 결재대기로 설정
        mapper.insertBoard(epboardVO);
    }

    @Override
    public int getNextSeq() {
        return mapper.getNextSeq();
    }

    @Override
    public List<EPBoardVO> getAllWrittenContent() {
        return mapper.getAllWrittenContent();
    }

    @Override
    public List<EPBoardVO> getWrittenContentByUser(String name) {
        return mapper.getWrittenContentByUser(name);
    }

    @Override
    public EPBoardVO getBoard(int seq) {
        return mapper.getBoard(seq);
    }

    @Override
    public void updateBoard(EPBoardVO epboardVO, String C_name) {
        epboardVO.setC_name(C_name);
        mapper.updateBoard(epboardVO);
    }

    
    public List<EPBoardVO> getVisibleContentByUser(String name) {
        return mapper.getVisibleContentByUser(name);
    }
    @Override
    public List<EPBoardVO> getAllVisibleContent(String userName) {
        return mapper.getAllVisibleContent(userName);
    }
    
    @Override
    public void insertHistory(EPHistoryVO historyVO) {
        mapper.insertHistory(historyVO);
    }

    @Override
    public List<EPHistoryVO> getHistoryBySeq(int seq) {
        return mapper.getHistoryBySeq(seq);
    }

	

	
    
}
