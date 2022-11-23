package com.joonyoung.rubato.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.joonyoung.rubato.dao.IDao;
import com.joonyoung.rubato.dto.FileDto;
import com.joonyoung.rubato.dto.RFBoardDto;
import com.joonyoung.rubato.dto.RMemberDto;
import com.joonyoung.rubato.dto.RReplyDto;

@Controller
public class HomeController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "/")
	public String home() {
		return "redirect:index";
	}
	
	@RequestMapping(value = "index")
	public String index(Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		List<RFBoardDto> boardDtos = dao.rfblist();// 전체 리스트 불러오기
		
		int boardSize = boardDtos.size();// 전체 글의 개수
		
		if(boardSize >=4) {
			boardDtos = boardDtos.subList(0, 4);
		} else {
			boardDtos = boardDtos.subList(0, boardSize);
		}
				
//		model.addAttribute("freeboard01", boardDtos.get(0));
//		model.addAttribute("freeboard02", boardDtos.get(1));
//		model.addAttribute("freeboard03", boardDtos.get(2));
//		model.addAttribute("freeboard04", boardDtos.get(3));
		
		model.addAttribute("boardDtos", boardDtos);
		
		return "index";
	}
	
	@RequestMapping(value = "board_list")
	public String board_list(Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		ArrayList<RFBoardDto> boardDtos = dao.rfblist();
		int boardCount = dao.rfboardAllCount();
		
		model.addAttribute("boardList", boardDtos);
		model.addAttribute("boardCount", boardCount);
		
		return "board_list";
	}
	
	@RequestMapping(value = "board_view")
	public String board_view(HttpServletRequest request, Model model, HttpSession session) {
		
		String rfbnum = request.getParameter("rfbnum");
		//사용자가 글리스트에서 클릭한 글의 번호
		
		IDao dao = sqlSession.getMapper(IDao.class);			
		
		dao.rfbhit(rfbnum);//조회수 증가
		
		RFBoardDto rfboardDto = dao.rfboardView(rfbnum);
		ArrayList<RReplyDto> replyDtos =  dao.rrlist(rfbnum);
		
		FileDto filedto = dao.getFileInfo(rfbnum);
		
		model.addAttribute("rfbView", rfboardDto);
		model.addAttribute("replylist", replyDtos);//해당 글에 달린 댓글 리스트
		model.addAttribute("filedto", filedto);//해당 글에 첨부된 파일의 모든 정보 dto 전송
		
		return "board_view";
	}
	
	@RequestMapping(value = "board_write")
	public String board_write(HttpSession session, HttpServletResponse response) {
		String sessionId = (String) session.getAttribute("memberId");
		if(sessionId == null) {//참이면 로그인이 안된 상태
			PrintWriter out;
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 글을 쓰실수 없습니다!');history.go(-1);</script>");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
		
		return "board_write";
	}
	
	@RequestMapping(value = "member_join")
	public String member_join() {
		return "member_join";
	}
	
	@RequestMapping(value = "joinOk")
	public String joinOk(HttpServletRequest request, HttpSession session) {
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		String memberName = request.getParameter("mname");
		String memberEmail = request.getParameter("memail");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.joinMember(memberId, memberPw, memberName, memberEmail);
		
		session.setAttribute("memberId", memberId);//가입과 동시에 로그인
		
		return "redirect:index";
	}
	
	@RequestMapping(value = "loginOk")
	public String loginOk(HttpServletRequest request, HttpSession session) {
		
		String memberId = request.getParameter("mid");
		String memberPw = request.getParameter("mpw");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		//int checkIdFlag = dao.checkUserId(memberId);
		int checkIdFlag = dao.checkUserIdAndPw(memberId, memberPw);//1이면 로그인ok, 0이면 로그인x
		
		if(checkIdFlag == 1) {//참이면 로그인 성공
			session.setAttribute("memberId", memberId);
			
		}
		
		return "redirect:index";
	}
	
	@RequestMapping(value = "logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:index";
	}
	
	@RequestMapping(value = "writeOk")
	public String writeOk(HttpServletRequest request, HttpSession session, @RequestPart MultipartFile files) throws IllegalStateException, IOException {
		
		String boardName = request.getParameter("rfbname");
		String boardTitle = request.getParameter("rfbtitle");
		String boardContent = request.getParameter("rfbcontent");
		
		String sessionId = (String) session.getAttribute("memberId");
		//글쓴이의 아이디는 현재 로그인된 유저의 아이디이므로 세션에서 가져와서 전달 
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		if(files.isEmpty()) { //파일의 첨부여부를 확인
			dao.rfbwrite(boardName, boardTitle, boardContent, sessionId, 0);
		} else {
			dao.rfbwrite(boardName, boardTitle, boardContent, sessionId, 1);
			ArrayList<RFBoardDto> latestBoard = dao.boardLatestInfo(sessionId);
			RFBoardDto dtos = latestBoard.get(0);
			int rfbnum = dtos.getRfbnum();
			
			//파일 첨부
			String fileoriname = files.getOriginalFilename();//첨부된 파일의 원래 이름
			String fileextention = FilenameUtils.getExtension(fileoriname).toLowerCase();
			//첨부된 파일의 확장자 추출 후 소문자로 강제 변경 
			File destinationFile;//java.io 패키지 제공 클래스 임포트
			String destinationFileName;// 실제 서버에 저장된 파일의 변경된 이름이 저장될 변수 선언
			String fileurl = "C:/springboot_workspace/rubatoProject-2022-11-17/src/main/resources/static/uploadfiles/";
			//첨부된 파일이 저장될 서버의 실재 폴더 경로
			
			do {
			destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileextention;
			//알파벳 대소문자와 숫자를 포함한 랜점 32자 문자열 생성 후 .을 구분자로 원본 파일의 확장자를 연결->실제 서버에 저장될 파일의 이름
			
			destinationFile = new File(fileurl+destinationFileName);
			} while(destinationFile.exists()) ;
			// 혹시 같은 이름의 파일 이름이 존재하는지 확인
			
			destinationFile.getParentFile().mkdir();
			files.transferTo(destinationFile);
			
			dao.fileInforInsert(rfbnum, fileoriname, destinationFileName, fileextention, fileurl);
		}
		return "redirect:board_list";
	}
	
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request) {
		
		String rfbnum = request.getParameter("rfbnum");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.delete(rfbnum);
		
		return "redirect:board_list";
	}
	
	@RequestMapping(value = "replyOk")
	public String replyOk(HttpServletResponse response, HttpServletRequest request, HttpSession session, Model model ) {
		
		String rrorinum = request.getParameter("rfbnum");//댓글이 달린 원글의 번호
		String rrcontent = request.getParameter("rrcontent");//댓글 내용
		
		String sessionId = (String) session.getAttribute("memberId");//현재 로그인한 유저의 아이디
		
		if(sessionId == null) {//참이면 로그인이 안된 상태
			PrintWriter out;
			try {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				out.println("<script>alert('로그인하지 않으면 글을 쓰실수 없습니다!');history.go(-1);</script>");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			
			IDao dao = sqlSession.getMapper(IDao.class);
			dao.rrwrite(rrorinum, sessionId, rrcontent);//댓글 쓰기
			dao.rrcount(rrorinum);//해당글의 댓글 총 개수 증가
			
			RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
			ArrayList<RReplyDto> replyDtos =  dao.rrlist(rrorinum);
			
			model.addAttribute("rfbView", rfboardDto);//원글의 게시글 내용 전부
			model.addAttribute("replylist", replyDtos);//해당 글에 달린 댓글 리스트
			
		}
		
		return "board_view";
	}
	
	@RequestMapping(value = "replyDelete")
	public String replyDelete(HttpServletRequest request, Model model) {
		
		String rrnum = request.getParameter("rrnum");//댓글 고유번호
		String rrorinum = request.getParameter("rfbnum");//댓글이 달린 원글의 고유번호
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.rrdelete(rrnum);//댓글 삭제
		dao.rrcountMinus(rrorinum);//해당 글의 댓글 갯수 1감소
		
		RFBoardDto rfboardDto = dao.rfboardView(rrorinum);
		ArrayList<RReplyDto> replyDtos =  dao.rrlist(rrorinum);
		
		model.addAttribute("rfbView", rfboardDto);//원글의 게시글 내용 전부
		model.addAttribute("replylist", replyDtos);//해당 글에 달린 댓글 리스트
		
		return "board_view";
	}
	
	@RequestMapping(value = "search_list")
	public String search_list(HttpServletRequest request, Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		ArrayList<RFBoardDto> boardDtos = null;
		
		String searchOption = request.getParameter("searchOption");
		String searchKey = request.getParameter("searchKey");
		
		if(searchOption.equals("title")) {
			boardDtos = dao.rfbSearchTitleList(searchKey);
		} else if(searchOption.equals("content")) {
			boardDtos = dao.rfbSearchContentList(searchKey);
		} else if(searchOption.equals("writer")) {
			boardDtos = dao.rfbSearchWriterList(searchKey);
		}
		
		model.addAttribute("boardList", boardDtos);
		model.addAttribute("boardCount", boardDtos.size());//검색 결과 게시물의 개수 반환
		
		return "board_list";
	}
	
	@RequestMapping("file_down")
		public String file_down(HttpServletRequest request, Model model, HttpServletResponse response) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		String rfbnum = request.getParameter("rfbnum");
		
		FileDto filedto = dao.getFileInfo(rfbnum);
		
		String filename = filedto.getFilename();
		
		PrintWriter out;
		try {
			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			out.println("<script>window.location.href='/resources/uploadfiles/" + filename + "'</script>");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:board_list";
	}
}
