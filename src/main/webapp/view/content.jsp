<%@page import="model.ReplyDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="model.MemberDTO"%>
<%@page import="model.MemberDAO"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
int no = Integer.parseInt(request.getParameter("no"));
MemberDAO dao = new MemberDAO();
MemberDTO member = new MemberDTO();
member = dao.getMember(no);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#page {
	background-color: antiquewhite;
	width: 80%;
	margin: 0 auto;
}

#top {
	text-align: center;
}

#middle {
	text-align: right;
}

#mainContents {
	background-color: white;
	width: 95%;
	height: 300px;
	border: 1px black solid;
	margin-left: 25px;
}

#innerContents {
	margin-left: 30px;
}

#crudButton {
	text-align: right;
	margin-right: 25px;
}
#replySector {
	width: 100%;
	border-top: 10px solid white;
}

#replyForm{
	width: 55%;
}

#replySubmit{
	text-align: right;
}

#repleyPrintSector{
	width: 100%;
	border-top: 10px solid white;
}

#replyTitle{
	width: 100%;
	text-align: center;
	border-bottom: 1px solid white;
}

#replyArea{
	text-align:center;
	width: 100%;
	border-bottom: 3px solid white;
}

#replyWriter{
	width: 22%;
	text-align: center;
}

#replyBtns{
	width: 100%;
	text-align: center;}
</style>
</head>
	<%
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("map");
		List<ReplyDTO> list = (List<ReplyDTO>) map.get("list2");
		int count = (int) map.get("count");
	%>
<body>
	<div id="page">
		<div id="top">
			<h1>
				제목 :
				<%=member.getTitle()%><br />
			</h1>
		</div>
		<hr />
		<div id="middle">
			<em>번호</em> : <strong><%=no%></strong>
			<em>작성자</em> : <strong><%=member.getWriter()%></strong>
			<em>작성일</em> : <strong><%=member.getDate()%></strong>
			<em>조회수</em> : <strong><%=member.getView()%></strong>
		</div>
		<br />
		<div id="mainContents">
			<p id="innerContents">
				<%=member.getContents()%>
			</p>
		</div>
			<br>
			<%
				if(member.getFile() == null){
					out.println("첨부파일 : <em>첨부된 파일이 없습니다.</em>");
				}else{%>
					첨부파일 : <a href="download.do?file=<%=member.getFile() %>"><%=member.getFile() %></a>
				<%}
				
			%>
		<div id="crudButton">
			<br />
			<button onClick="location.href='update.do?no=<%=no%>'">수정</button>
			<button onClick="location.href='delete.do?no=<%=no%>'">삭제</button>
			<button onClick="location.href='list.do'">목록</button>
			<button onClick="location.href='answerWriteFront.do?motherNo=<%=no%>'">답글</button>
		</div>
		<div id="replySector">
			<form action="replyInsert.do?no=<%=no %>" id="replyForm" method="post">
				작성자 : <input type="text" name="replyWriter"><br /><br />
				댓글 : <textarea name="replyContent" id="" cols="80" rows="10"></textarea> <br /><br />
				비밀번호 : <input type="password" name="password">
				비밀번호 확인 : <input type="password" name="passwordCheck">
				<div id="replySubmit">
					<input  type="submit" value="등록">	
				</div>
			</form>
		</div>
		<div id="repleyPrintSector">
			<div id="replyTitle"><h2>댓글</h2></div>
			<%for(ReplyDTO rd : list) { %>
				<div id="replyArea">
					<form method="post">
						<div id="replyWriter">작성자 : <input type="text" value="<%=rd.getReplyWriter() %>" name="changedReplyWriter"><br /> <br /></div>
						댓글 : <textarea cols="160" name="changedReplyContent"><%=rd.getReplyContent() %></textarea><br /><br /><br />
						<div id="replyWriter">비밀번호 : <input type="password" name="inputPassword"></div>
						<div id="replyBtns">
							<input type="submit" formaction="replyUpdate.do?no=<%=rd.getNo() %>&upNo=<%=no %>" value="수정">
							<input type="submit" formaction="replyDelete.do?no=<%=rd.getNo() %>&upNo=<%=no %>" value="삭제">
						</div>
					</form>
				</div>
			<%} %>
		</div>
	</div>
</body>
</html>







