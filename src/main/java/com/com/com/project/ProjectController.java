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
    public String main(HttpServletRequest request, Model model) {
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
    public String write(EPBoardVO epboardVO, @RequestParam("action") String action, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");
        String grade = (String) session.getAttribute("grade");

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
        return "redirect:/main";
    }




    @GetMapping("/EPView")
    public String epview(@RequestParam("seq") int seq, Model model, HttpServletRequest request) {
        EPBoardVO epboardVO = service.getBoard(seq);
        HttpSession session = request.getSession();
        String grade = (String) session.getAttribute("grade");

        model.addAttribute("epboardVO", epboardVO);
        model.addAttribute("grade", grade); // 등급 정보 추가

        return "/project/view";
    }

    @PostMapping("/approve")
    public String approve(@RequestParam("seq") int seq, @RequestParam("c_name") String cName, HttpServletRequest request) {
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
        return "redirect:/main";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam("seq") int seq, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");

        EPBoardVO epboardVO = service.getBoard(seq);
        epboardVO.setWait("반려");
        epboardVO.setC_name(name);

        service.updateBoard(epboardVO, name);

        return "redirect:/main";
    }

}
