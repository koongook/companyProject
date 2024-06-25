package com.com.com.board;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nexacro.java.xapi.data.DataSet;
import com.nexacro.java.xapi.data.DataTypes;
import com.nexacro.java.xapi.data.PlatformData;
import com.nexacro.java.xapi.data.VariableList;
import com.nexacro.java.xapi.tx.HttpPlatformRequest;
import com.nexacro.java.xapi.tx.HttpPlatformResponse;
import com.nexacro.java.xapi.tx.PlatformException;

@Controller
public class BoardController {

	@Autowired
	private Service service;


	@RequestMapping(value = "/write_form", method = RequestMethod.GET)
	public String write_form() {
		return "/crud/write";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(BoardVO boardVO, @RequestParam(value = "files", required = false) MultipartFile[] files) throws Exception {
		if(files !=null && files.length > 0) {
			service.write(boardVO, files);
			System.out.println("낫널ㄹㄹㄹㄹㄹㄹㄹㄹ");
		} else {
			service.write(boardVO);
			System.out.println("비었어파일이 널");
		}
		return "redirect:/board/search";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(@RequestParam("seq") int seq, Model model) {
		BoardVO boardVO = service.getBoard(seq);
		List<FileVO> fileVOList = service.getFile(seq);
		service.updateViewCnt(seq);
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("fileVOList", fileVOList);
		return "/crud/view";
	}

	@RequestMapping(value = "/update_form", method = RequestMethod.GET)
	public String update_form(@RequestParam("seq") int seq, Model model) {
		BoardVO boardVO = service.getBoard(seq);
		List<FileVO> fileVOList = service.getFile(seq);
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("fileVOList", fileVOList);
		return "/crud/update";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public  String update(BoardVO boardVO, @RequestParam(value = "files", required = false) MultipartFile files, Model model) {
	    // 파일이 존재하는지 확인
	    if (files != null && !files.isEmpty()) {
	        // 파일 업로드 및 저장
	        try {
	            // 기존 파일 삭제
	            List<FileVO> existingFiles = service.getFile(boardVO.getSeq());
	            for (FileVO existingFile : existingFiles) {
	                FileManager.deleteFile(existingFile.getSave_name());
	            }

	            // 새 파일 업로드
	            String originalFilename = files.getOriginalFilename();
	            String savedFilename = FileManager.saveFile(files);
	            Date now = new Date();

	            // 파일 정보 업데이트
	            FileVO newFile = new FileVO();
	            newFile.setReal_name(originalFilename);
	            newFile.setSave_name(savedFilename);
	            newFile.setreg_date(now);
	            newFile.setSave_path("C:\\imgFile\\"); // 실제 파일이 저장된 경로
	            newFile.setList_seq(boardVO.getSeq()); // 게시글의 일련 번호와 연결

	            // 파일 정보 DB에 저장
	            service.updateBoardWithFile(boardVO, newFile);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    } else {
	        // 파일이 없는 경우 기존 파일을 유지하도록 서비스에 업데이트 요청
	        service.update(boardVO);
	    }

	    return "redirect:/board/search";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam("seq") int seq, Model model) {
		service.delete(seq);
		return "redirect:/board/search";

	}

	@RequestMapping(value = "/deleteSelected", method = RequestMethod.POST)
	public String multi(@RequestParam List<String> chk, Model model) {
		for (String c : chk) {
			service.delete(Integer.parseInt(c));
		}
		return "redirect:/board/search";
	}

	@GetMapping("/boardList")
	public String boardList(PagingVO vo, Model model, @RequestParam(value = "nowPage", required = false) String nowPage,
			@RequestParam(value = "cntPerPage", required = false) String cntPerPage) {

		int total = service.countBoard();
		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "10";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) {
			cntPerPage = "10";
		}
		vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
		model.addAttribute("paging", vo);
		model.addAttribute("boardList", service.selectBoard(vo));
		return "/crud/list";
	}
	
	@GetMapping("/board/search")
	public String search(PagingVO vo, Model model, 
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "nowPage", required = false) String nowPage,
			@RequestParam(value = "cntPerPage", required = false) String cntPerPage,
			@RequestParam(value = "startDate", required = false) String startDateStr, 
            @RequestParam(value = "endDate", required = false) String endDateStr) {

		if (nowPage == null && cntPerPage == null) {
			nowPage = "1";
			cntPerPage = "10";
		} else if (nowPage == null) {
			nowPage = "1";
		} else if (cntPerPage == null) {
			cntPerPage = "10";
		}


		if (type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
			int total = service.countSearched(type, keyword);
			vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
			model.addAttribute("paging", vo);
			model.addAttribute("boardList", service.selectSearch(vo, type, keyword));
		} 
		
		else if(startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !startDateStr.isEmpty()) {
			try {
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        Date startDate = sdf.parse(startDateStr);
		        Date endDate = sdf.parse(endDateStr);
		        
		        int total = service.countSearchedByDate(startDate, endDate);
				vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage)); 
				model.addAttribute("paging", vo);
		        model.addAttribute("boardList", service.searchByDate(vo, startDate, endDate));
		        model.addAttribute("startDate", startDateStr);
		        model.addAttribute("endDate", endDateStr);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		} 
		
		else {
			int total = service.countBoard();
			vo = new PagingVO(total, Integer.parseInt(nowPage), Integer.parseInt(cntPerPage));
			model.addAttribute("paging", vo);
			model.addAttribute("boardList", service.selectBoard(vo));
		}
		model.addAttribute("type", type);
		model.addAttribute("keyword", keyword);
		
		
		return "/crud/list";
	}
	 // 파일 다운로드 컨트롤러 메서드
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String fileName) throws IOException {
        // 파일 경로 설정 (실제 파일이 저장된 경로에 맞게 수정해야 합니다)
        String directory = "C:\\imgFile\\";
        String filePath = directory + fileName;

        // 파일이 존재하는지 확인
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("파일을 찾을 수 없습니다: " + fileName);
        }

        // 파일 다운로드를 위한 HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        // 파일을 FileSystemResource로 변환하여 ResponseEntity로 반환
        Path path = Paths.get(filePath);
        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(resource);
    }
    
    @RequestMapping("nexList")
    public void nexList(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        HttpPlatformRequest req = new HttpPlatformRequest(request);
        req.receiveData();  // 추가: 요청 데이터 수신

        List<BoardVO> list = service.getBoardList();

        DataSet ds = new DataSet("javaList");
        ds.addColumn("seq", DataTypes.INT, 100);
        ds.addColumn("id", DataTypes.STRING, 100);
        ds.addColumn("name", DataTypes.STRING, 100);
        ds.addColumn("subject", DataTypes.STRING, 100);
        ds.addColumn("content", DataTypes.STRING, 100);
        ds.addColumn("reg_date", DataTypes.STRING, 100);
        ds.addColumn("upt_date", DataTypes.STRING, 100);
        ds.addColumn("view_cnt", DataTypes.INT, 100);

        for (BoardVO boardVO : list) {
            int row = ds.newRow();
            ds.set(row, "seq", boardVO.getSeq());
            ds.set(row, "id", boardVO.getMem_id());
            ds.set(row, "name", boardVO.getMem_name());
            ds.set(row, "subject", boardVO.getBoard_subject());
            ds.set(row, "content", boardVO.getBoard_content());
            ds.set(row, "reg_date", boardVO.getReg_date() != null ? boardVO.getReg_date().toString() : "");
            ds.set(row, "upt_date", boardVO.getUpt_date() != null ? boardVO.getUpt_date().toString() : "");
            ds.set(row, "view_cnt", boardVO.getView_cnt() != null ? Integer.parseInt(boardVO.getView_cnt()) : 0);
        }

        PlatformData pData = new PlatformData();
        pData.addDataSet(ds);

        HttpPlatformResponse res = new HttpPlatformResponse(response, req);
        res.setData(pData);
        res.sendData();
    }
    @RequestMapping("nexSearch")
    public void nexSearch(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
        HttpPlatformRequest req = new HttpPlatformRequest(request);
        req.receiveData(); // 요청 데이터 수신

        PlatformData inPd = req.getData(); // PlatformData를 통해 데이터를 받음
        VariableList inVl = inPd.getVariableList(); // VariableList 추출

        String type = inVl.getString("type");
        String keyword = inVl.getString("keyword");

        // 전달된 값 확인을 위한 출력
        System.out.println("Received type: " + type);
        System.out.println("Received keyword: " + keyword);

        // 검색어와 타입을 통해 검색 결과 가져오기
        DataSet resultDataSet = service.searchBoardAsDataSet(type, keyword);

        PlatformData pData = new PlatformData();
        pData.addDataSet(resultDataSet);

        HttpPlatformResponse res = new HttpPlatformResponse(response, req);
        res.setData(pData);
        res.sendData();
    }


    



}
    



	

