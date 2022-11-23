<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>클래식기타 커뮤니티</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/header.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/footer.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/board_left.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/board_write_main.css">
</head>
<body>
	<% 
		String sessionId = (String) session.getAttribute("memberId");
		//로그인중이라면 로그인한 아이디가 저장되고 비로그인 중이면 sessionId==null 임
	%>
  <div id="wrap">
    <header> <!-- header 시작 -->
      <a href="index"><img id="logo" src="${pageContext.request.contextPath}/resources/img/logo.png"></a>
      <nav id="top_menu">
        HOME | LOGIN | JOIN | NOTICE
      </nav>
      <nav id="main_menu">
        <ul>
          <li><a href="board_list">자유게시판</a></li>
          <li><a href="#">기타 연주</a></li>
          <li><a href="#">공동 구매</a></li>
          <li><a href="#">연주회 안내</a></li>
          <li><a href="#">회원 게시판</a></li>
        </ul>
      </nav>
    </header> <!-- header 끝 -->
    <aside>
      <article id="login_box"> <!-- login box 시작 -->
        <img id="login_title" src="${pageContext.request.contextPath}/resources/img/ttl_login.png">
        
         <% if(sessionId == null) { %>
        <form action="loginOk">
        <div id="input_button">
          <ul id="login_input">
            <li><input type="text" name="mid"></li>
            <li><input type="password" name="mpw"></li>
          </ul>
          <input type="image" id="login_btn" src="${pageContext.request.contextPath}/resources/img/btn_login.gif">
        </div>
        </form>
        <% } else { %>
        <br><%= sessionId %>님 로그인 중<br>        
        <% } %>
        <div class="clear"></div>
        <% if(sessionId == null) { %>
        <div id="join_search">
          <a href="member_join"><img src="${pageContext.request.contextPath}/resources/img/btn_join.gif"></a>
          <img src="${pageContext.request.contextPath}/resources/img/btn_search.gif">
        </div>
        <% } else {%>
        <a href="logout">로그아웃</a>
        <% } %>
      </article> <!-- login box 끝 -->
      <nav id="sub_menu"> <!-- 서브 메뉴 시작 -->
        <ul>
          <li><a href="board_list">+ 자유 게시판</a></li>
          <li><a href="#">+ 방명록</a></li>
          <li><a href="#">+ 공지사항</a></li>
          <li><a href="#">+ 등업 요청</a></li>
          <li><a href="#">+ 포토갤러리</a></li>
        </ul>
      </nav> <!-- 서브 메뉴 끝 -->
      <article id="sub_banner">
        <ul>
          <li><img src="${pageContext.request.contextPath}/resources/img/banner1.png"></li>
          <li><img src="${pageContext.request.contextPath}/resources/img/banner2.png"></li>
          <li><img src="${pageContext.request.contextPath}/resources/img/banner3.png"></li>
        </ul>
      </article>
    </aside>
    <main>
      <section id="main">
        <img src="${pageContext.request.contextPath}/resources/img/comm.gif">
        <h2 id="board_title">자유게시판</h2>
        <div id="write_title"><h2>글쓰기</h2></div>
        <form action="writeOk" method="post" enctype="multipart/form-data">
        <table>
          <tr id="name">
            <td class="col1">이름</td>
            <td class="col2"><input type="text" name="rfbname"></td>
          </tr>
          <tr id="subject">
            <td class="col1">제목</td>
            <td class="col2"><input type="text" name="rfbtitle"></td>
          </tr>
          <tr id="content">
            <td class="col1">내용</td>
            <td class="col2"><textarea name="rfbcontent"></textarea></td>
          </tr>
          <tr id="upload">
            <td class="col1">업로드 파일</td>
            <td class="col2"><input type="file" name="files"></td>
          </tr>
        </table>
        <div id="buttons">
          <input type="image" src="${pageContext.request.contextPath}/resources/img/ok.png">
          <a href="board_list"><img src="${pageContext.request.contextPath}/resources/img/list.png"></a>
        </div>
        </form>
      </section> <!-- section main 끝 -->
    </main>
    <div class="clear"></div>
    <footer> <!-- footer 시작 -->
      <img id="footer_logo" src="${pageContext.request.contextPath}/resources/img/footer_logo.gif">
      <ul id="address">
        <li>서울시 강남구 삼성동 1234 (우) : 123-1234</li>
        <li>TEL : 02-1234-1234 Email : abc@abc.com</li>
        <li id="copyright">COPYRIGHT(C) 루바토 ALL RIGHTS RESERVED</li>
      </ul>
      <ul id="footer_sns">
        <li><img src="${pageContext.request.contextPath}/resources/img/facebook.gif"></li>
        <li><img src="${pageContext.request.contextPath}/resources/img/blog.gif"></li>
        <li><img src="${pageContext.request.contextPath}/resources/img/twitter.gif"></li>
      </ul>
    </footer> <!-- footer 끝 -->
  </div>
</body>
</html>