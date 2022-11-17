package com.joonyoung.rubato.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joonyoung.rubato.dao.IDao;

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
}
