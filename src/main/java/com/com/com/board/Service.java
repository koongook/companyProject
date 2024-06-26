package com.com.com.board;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nexacro.java.xapi.data.DataSet;
import com.nexacro.java.xapi.data.DataTypes;




@org.springframework.stereotype.Service
public class Service{
	@Autowired
	private BoardMapper mapper;
	@Autowired
	private FileManager fileManager;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Service.class);


	public List<BoardVO> getBoardList() {
		return mapper.getList2();
	}
	public void write(BoardVO boardVO) throws IOException {
		mapper.insertBoard(boardVO);
	}
	
	@Transactional
	public void write(BoardVO boardVO, MultipartFile[] files) throws IOException {
		mapper.insertBoard(boardVO);
		
		// �뙆�씪 �뾽濡쒕뱶 諛� ���옣
		if (files != null && files.length > 0) {
	        for (MultipartFile file : files) {
	            if (!file.isEmpty()) {
	                String originalFilename = file.getOriginalFilename();
	                String savedFilename = fileManager.saveFile(file);
	                Date now = new Date();
	                
	                FileVO fileVO = new FileVO();
	                fileVO.setReal_name(originalFilename);
	                fileVO.setSave_name(savedFilename);
	                fileVO.setreg_date(now);
	                fileVO.setSave_path("C:\\imgFile\\"); // �떎�젣 �뙆�씪�씠 ���옣�맂 寃쎈줈
	                
	                mapper.insertFile(fileVO); // �뙆�씪 �젙蹂대�� DB�뿉 ���옣
	            }
	        }
	    }
	}
	
	public BoardVO getBoard(int seq) {
		
		return mapper.getBoard(seq);
	}
	public List<FileVO> getFile(int seq) {
		return mapper.getFile(seq);
	}
	public void update(BoardVO boardVO) {
		mapper.update(boardVO);
	}
	public void delete(int seq) {
		mapper.delete(seq);
	}
	
	public void updateViewCnt(int seq) {
		mapper.updateViewCnt(seq);	
	}
	// 寃뚯떆臾� 珥� 媛��닔
	public int countBoard() {
		return mapper.countBoard();
	};
	public int countSearched(String type, String keyword) {
		return mapper.countSearched(type, keyword);
	}
	// �럹�씠吏� 泥섎━ 寃뚯떆湲� 議고쉶
	public List<BoardVO> selectBoard(PagingVO vo) {
		return mapper.selectBoard(vo);
	}
	public List<BoardVO> selectSearch(PagingVO vo, String type, String keyword) {
		return mapper.selectSearch(vo, type, keyword);
	}
//	public List<BoardVO> searchByDate(Date startDate, Date endDate) {
//        return mapper.searchByDate(startDate, endDate);
//    }
	public List<BoardVO> searchByDate(PagingVO vo, @Param("startDate") Date startDate, @Param("endDate") Date endDate) {
		return mapper.searchByDate(vo, startDate, endDate);
	}
	public int countSearchedByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate) {
		return mapper.countSearchedByDate(startDate, endDate);
	}
	
	@Transactional
	public void updateBoardWithFile(BoardVO boardVO, FileVO newFile) throws IOException {
	    // 寃뚯떆湲� �뾽�뜲�씠�듃
	    mapper.update(boardVO);

	    // �뙆�씪 �젙蹂� �뾽�뜲�씠�듃
	    mapper.deleteFilesByListSeq(boardVO.getSeq()); // 湲곗〈 �뙆�씪 �젙蹂� �궘�젣
	    mapper.insertFile(newFile); // �깉 �뙆�씪 �젙蹂� 異붽�
	}
	
	public List<BoardVO> searchBoard(String type, String keyword) {
        return mapper.searchBoard(type, keyword);
    }
	
//	public DataSet searchBoardAsDataSet(String type, String keyword) {
//	    List<BoardVO> list = mapper.searchBoard(type, keyword);
//
//	    DataSet ds = new DataSet("javaSearch");
//	    ds.addColumn("seq", DataTypes.INT, 100);
//	    ds.addColumn("id", DataTypes.STRING, 100);
//	    ds.addColumn("name", DataTypes.STRING, 100);
//	    ds.addColumn("subject", DataTypes.STRING, 100);
//	    ds.addColumn("content", DataTypes.STRING, 100);
//	    ds.addColumn("reg_date", DataTypes.STRING, 100);
//	    ds.addColumn("upt_date", DataTypes.STRING, 100);
//	    ds.addColumn("view_cnt", DataTypes.INT, 100);
//
//	    for (BoardVO boardVO : list) {
//	        int row = ds.newRow();
//	        ds.set(row, "seq", boardVO.getSeq());
//	        ds.set(row, "id", boardVO.getMem_id());
//	        ds.set(row, "name", boardVO.getMem_name());
//	        ds.set(row, "subject", boardVO.getBoard_subject());
//	        ds.set(row, "content", boardVO.getBoard_content());
//	        ds.set(row, "reg_date", boardVO.getReg_date() != null ? boardVO.getReg_date().toString() : "");
//	        ds.set(row, "upt_date", boardVO.getUpt_date() != null ? boardVO.getUpt_date().toString() : "");
//	        ds.set(row, "view_cnt", boardVO.getView_cnt() != null ? Integer.parseInt(boardVO.getView_cnt()) : 0);
//	    }
//
//	    return ds;
//	}

}


