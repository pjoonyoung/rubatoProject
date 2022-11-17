package com.joonyoung.rubato.controller;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
