package com.com.com.project;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProjectController {

    @Autowired
    private ProjectServiceImpl service;

    @GetMapping("/project")
    public String project(HttpServletRequest request) {
        String error = request.getParameter("error");
        request.setAttribute("error", error);
        return "/project/eplogin";
    }

    @PostMapping("/eplogin")
    public String login(@RequestParam("id") String id, @RequestParam("password") int password, HttpServletRequest request) {
        MemberVO member = service.getMember(id);

        if (member == null) {
            return "redirect:/project?error=invalid_id";
        } else if (member.getPassword() != password) {
            return "redirect:/project?error=incorrect_password";
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("id", id);
            session.setAttribute("name", member.getName());
            session.setAttribute("grade", member.getGrade());
            return "redirect:/main";
        }
    }

    @GetMapping("/main")
    public String main(@RequestParam(value = "searchType", required = false) String searchType,
                       @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                       @RequestParam(value = "approvalStatus", required = false) String approvalStatus,
                       @RequestParam(value = "dateRange", required = false) String dateRange,
                       HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");

        if (id != null) {
            MemberVO member = service.getMember(id);
            List<EPBoardVO> writtenContent;

            if ("과장".equals(member.getGrade()) || "부장".equals(member.getGrade())) {
                writtenContent = service.getAllVisibleContent(member.getName());
            } else {
                writtenContent = service.getWrittenContentByUser(member.getName());
            }

            // 검색 조건 적용
            String startDate = null;
            String endDate = null;
            if (dateRange != null && !dateRange.isEmpty()) {
                String[] dates = dateRange.split(" - ");
                if (dates.length == 2) {
                    startDate = dates[0].trim();
                    endDate = dates[1].trim();
                } else {
                    // 날짜 형식이 잘못된 경우, 로그 추가
                    System.err.println("Invalid dateRange format: " + dateRange);
                }
            }

            if ((searchType != null && !searchType.isEmpty()) || 
                (searchKeyword != null && !searchKeyword.isEmpty()) || 
                (approvalStatus != null && !approvalStatus.isEmpty()) || 
                (startDate != null && endDate != null)) {
                writtenContent = service.searchContent(searchType, searchKeyword, approvalStatus, startDate, endDate, member.getName(), member.getGrade());
            }

            model.addAttribute("writtenContent", writtenContent);
            model.addAttribute("memberList", member);
            return "/project/main";
        } else {
            return "redirect:/project";
        }
    }





    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/project";
    }

    @GetMapping("/writeForm")
    public String writeForm(HttpServletRequest request) {
        int seq = service.getNextSeq();
        request.setAttribute("seq", seq);
        return "/project/write";
    }

    @PostMapping("/EPwrite")
    public String write(EPBoardVO epboardVO, EPHistoryVO historyVO, @RequestParam("action") String action, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");
        String grade = (String) session.getAttribute("grade");
        epboardVO.setGrade(grade);
        epboardVO.setName(name);
        if ("임시저장".equals(action)) {
            epboardVO.setWait("임시저장");
        } else if ("결재".equals(action)) {
            if ("과장".equals(grade)) {
                epboardVO.setWait("결재중");
                epboardVO.setCom_date(new Timestamp(System.currentTimeMillis()));
                epboardVO.setC_name(name); // 결재자 이름 설정
            } else if ("부장".equals(grade)) {
                epboardVO.setWait("결재완료");
                epboardVO.setCom_date(new Timestamp(System.currentTimeMillis())); // 결재 완료 시 결재 날짜 설정
                epboardVO.setC_name(name); // 결재자 이름 설정
            } else {
                epboardVO.setWait("결재대기");
            }
        }

        System.out.println("EPBoardVO: " + epboardVO); // 디버깅 출력

        service.write(epboardVO);
        service.updateBoard(epboardVO, name);
        System.out.println(1);

        // 히스토리 저장
        historyVO.setH_seq(epboardVO.getSeq());
        historyVO.setReg_date(new Timestamp(System.currentTimeMillis()));
        historyVO.setName(name);
        historyVO.setWait(epboardVO.getWait());
        System.out.println(2);
        service.insertHistory(historyVO);
        System.out.println(3);

        return "redirect:/main";
    }




    @GetMapping("/EPView")
    public String epview(@RequestParam("seq") int seq, Model model, HttpServletRequest request) {
        EPBoardVO epboardVO = service.getBoard(seq);
        HttpSession session = request.getSession();
        String grade = (String) session.getAttribute("grade");

        model.addAttribute("epboardVO", epboardVO);
        model.addAttribute("grade", grade);

        String name = (String) session.getAttribute("name");
        boolean isAuthor = name.equals(epboardVO.getName()); // Check if logged-in user is the author
        model.addAttribute("isAuthor", isAuthor);
        
        List<EPHistoryVO> historyList = service.getHistoryBySeq(seq);
        model.addAttribute("historyList", historyList);
        
        if (isAuthor && ("반려".equals(epboardVO.getWait()) || "임시저장".equals(epboardVO.getWait()))) {
            return "/project/update";
        }

        return "/project/view";
    }

    @PostMapping("/approve")
    public String approve(@RequestParam("seq") int seq,EPHistoryVO historyVO, @RequestParam("c_name") String cName, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String grade = (String) session.getAttribute("grade");
        String name = (String) session.getAttribute("name");

        EPBoardVO epboardVO = service.getBoard(seq);
        if ("과장".equals(grade)) {
            epboardVO.setWait("결재중");
            epboardVO.setC_name(cName);
            epboardVO.setCom_date(new Timestamp(System.currentTimeMillis()));
            service.updateBoard(epboardVO, name);
        } else if ("부장".equals(grade)) {
            epboardVO.setWait("결재완료");
            epboardVO.setC_name(cName);
            epboardVO.setCom_date(new Timestamp(System.currentTimeMillis())); // 결재 완료 시 결재 날짜 설정
            service.updateBoard(epboardVO, name);
        }
        service.updateBoard(epboardVO, name);
        System.out.println(1);
        // 히스토리 저장
        historyVO.setH_seq(epboardVO.getSeq());
        historyVO.setReg_date(new Timestamp(System.currentTimeMillis()));
        historyVO.setName(name);
        historyVO.setWait(epboardVO.getWait());
        System.out.println(2);
        service.insertHistory(historyVO);
        System.out.println(3);
        return "redirect:/main";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam("seq") int seq, HttpServletRequest request, EPHistoryVO historyVO) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");

        EPBoardVO epboardVO = service.getBoard(seq);
        epboardVO.setWait("반려");
        epboardVO.setC_name(name);
        historyVO.setH_seq(epboardVO.getSeq());
        historyVO.setReg_date(new Timestamp(System.currentTimeMillis()));
        historyVO.setName(name);
        historyVO.setWait(epboardVO.getWait());
        
        service.insertHistory(historyVO);
        service.updateBoard(epboardVO, name);

        return "redirect:/main";
    }
    
    @PostMapping("/updateEP")
    public String updateEP(EPBoardVO epboardVO, EPHistoryVO historyVO, @RequestParam("action") String action, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");
        String grade = (String) session.getAttribute("grade");

        // Get existing board details
        EPBoardVO existingBoard = service.getBoard(epboardVO.getSeq());

        // Check if the logged-in user is the author
        boolean isAuthor = name.equals(existingBoard.getName());

        // Set isAuthor attribute to the model
        model.addAttribute("isAuthor", isAuthor);

        // Proceed with update logic based on isAuthor
        epboardVO.setGrade(grade);
        epboardVO.setName(name);

        if ("임시저장".equals(action)) {
            epboardVO.setWait("임시저장");
            if (!isAuthor) {
                epboardVO.setC_name(name);
            }
        } else if ("결재".equals(action)) {
            if ("과장".equals(grade)) {
                epboardVO.setWait("결재중");
                epboardVO.setCom_date(new Timestamp(System.currentTimeMillis()));
                epboardVO.setC_name(name);
            } else if ("부장".equals(grade)) {
                epboardVO.setWait("결재완료");
                epboardVO.setCom_date(new Timestamp(System.currentTimeMillis()));
                epboardVO.setC_name(name);
            } else {
                epboardVO.setWait("결재대기");
            }
        }

        // Update the board
        service.updateBoard(epboardVO, name);

        // Save history
        historyVO.setH_seq(epboardVO.getSeq());
        historyVO.setReg_date(new Timestamp(System.currentTimeMillis()));
        historyVO.setName(name);
        historyVO.setWait(epboardVO.getWait());
        service.insertHistory(historyVO);

        return "redirect:/main";
    }
    
    @GetMapping("/delegateApproval")
    public String getDelegateApprovalPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");
        String grade = (String) session.getAttribute("grade");
        model.addAttribute("name", name);
        model.addAttribute("grade", grade);
        return "project/delegateApproval";
    }

    @PostMapping("/delegateApproval")
    public String postDelegateApproval(@RequestParam("delegate") String delegate,
                                       @RequestParam("substitute") String substitute,
                                       @RequestParam("grade") String grade,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");

        // 대리 결재 처리 로직 (여기에 필요한 로직을 추가하세요)
        System.out.println("대리결재자: " + delegate);
        System.out.println("대리자: " + substitute);
        System.out.println("결재자 이름: " + name);

        // 성공적으로 처리된 후 메인 페이지로 리디렉션
        return "redirect:/main";
    }
    
    @GetMapping("/getGradeByName")
    @ResponseBody
    public String getGradeByName(@RequestParam("name") String name) {
        MemberVO member = service.getMemberByName(name);
        if (member != null) {
            return member.getGrade();
        }
        return "";
    }






}
