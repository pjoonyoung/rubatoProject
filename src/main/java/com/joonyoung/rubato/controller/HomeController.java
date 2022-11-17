package com.joonyoung.rubato.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joonyoung.rubato.dao.IDao;
import com.joonyoung.rubato.dto.RMemberDto;

@Controller
public class HomeController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("index")
	public String index() {
		
		return "index";
	}
	
	@RequestMapping("board_write")
	public String board_write() {
		
		return "board_write";
	}
	
	@RequestMapping("board_view")
	public String board_view() {
		
		return "board_view";
	}
	
	@RequestMapping("board_list")
	public String board_list() {
		
		return "board_list";
	}
	
	@RequestMapping("member_join")
	public String member_join() {
		
		return "member_join";
	}
	
	@RequestMapping("joinOk")
	public String joinOk(HttpServletRequest request) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		String mid = request.getParameter("mid");
		String mpw = request.getParameter("mpw");
		String mname = request.getParameter("mname");
		String memail = request.getParameter("memail");
		
		dao.joinMember(mid, mpw, mname, memail);
		
		return "joinOk";
	}
	
	@RequestMapping("loginOk")
	public String loginOk(HttpServletRequest request, Model model, HttpSession session) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		String mid = request.getParameter("mid");
		String mpw = request.getParameter("mpw");
		
		int checkIdFlag = dao.checkUserIdAndPw(mid, mpw);// 1이면 로그인Ok, 0이면 로그인x
		
		if(checkIdFlag == 1) {
			session.setAttribute("mid", mid);
		}
		
		return "redirect:index";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:index";
	}
	
}
