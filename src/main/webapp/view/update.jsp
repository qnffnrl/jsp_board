<%@page import="model.MemberDTO"%>
<%@page import="model.MemberDAO"%>
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

#listBtn{
	position: relative;
	left: 1400px;
	bottom: 24px;
}

</style>
</head>
<body>
	<div id="page">
		<form action="updateComplite.do" method="post" id="form">
			<!-- Update 되는값들 전송 폼 -->
			<div id="top">
				<h1>
					제목 : <input type="text" name="changeTitle" value="<%=member.getTitle()%>">
				</h1>
			</div>
			<hr />
			<div id="middle">
				<em>번호</em> : <strong><%=no%></strong> <input type="hidden"
					name="no" value="<%=no%>"> <em>작성자</em> : <input
					type="text" name="changeWriter" value="<%=member.getWriter()%>">
				<em>조회수</em> : <strong><%=member.getView()%></strong>
			</div>
			<br />
			<textarea id="mainContents" name="changeContents"><%=member.getContents()%></textarea>
			<div id="crudButton">
				<br /> <input type="submit" value="확인">
			</div>
		</form>
		<button id="listBtn" onClick="location.href='list.do'">목록</button>
	</div>
</body>
</html>