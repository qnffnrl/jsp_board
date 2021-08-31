<%@page import="model.MemberDAO"%>
<%@page import="model.MemberDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#page {
	background-color: antiquewhite;
	width: 80%;
	height: auto;
	margin: 0 auto;
	text-align: center;
	margin: 0 auto;
}

#title {
	text-decoration: none;
	color: black;
}

#title:hover {
	background-color: tomato;
}

#table {
	text-align: center;
	width: 100%;
	height: 400px;
	border-collapse: collapse;
	border-bottom: white;
	border-style : 1px solid white;
	border-right: none;
	border-left: none;
}

th {
	border-top: midnightblue solid;
	border-bottom: midnightblue solid;
	background-color: cadetblue;
}

td{
	font-size: 15px;
}

#pageNumber {
	text-align: right;
}

#titleAnker {
	text-decoration: none;
	color: black;
}

#titleAnker:hover {
	color: red;
}

#topSector {
	width: 100%;
}

#count {
	display: inline-block;
	float: left;
}

#search {
	float: right;
}

#btn {
	float: right;
	border: 1px solid black;
}

#btn:hover {
	background-color: black;
	color: white;
}

#searchBtn {
	border: 1px solid black;
}

#searchBtn:hover {
	background-color: black;
	color: white;
}

#pageNumberSector>a {
	font-weight: bolder;
	text-decoration: none;
	color: white;
	border: black 1px solid;
	background-color: cadetblue;
}

#pageNumberSector>a:hover {
	background-color: black;
	color: white;
}

#h2 {
	display: inline-block;
}

#pagingBtn {
	text-decoration: none;
	color: black;
}

#pagingBtn:hover {
	background-color: black;
	color: white;
}

#bottomSector {
	width: 100%;
}
</style>
</head>
<body>
	<%
	Map<String, Object> map = (Map<String, Object>) request.getAttribute("map");
	List<MemberDTO> list = (List<MemberDTO>) map.get("list");
	int count = (int) map.get("count");
	%>

	<div id="page">
		<a id="titleAnker" href="list.do"><h1><em>JSP_Notice Board</em></h1></a>
		<div id="topSector">
			<span id="count">전체<%=count%>건
			</span>
			<form id="search" action="list.do" method="get">
				<select name="option">
					<option name="title" value="title">제목</option>
					<option name="writer2" value="writer">작성자</option>
				</select> <input type="text" name="searchText"> <input id="searchBtn"
					type="submit" value="검색">
			</form>
			<br> <br>
		</div>
		<table id="table" border="1" width="100%" height="400">
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>날짜</th>
					<th>조회수</th>
				</tr>
			</thead>
			<%
			for (MemberDTO mb : list) {
			%>
			<tbody>
				<tr>
					<td>
						<%
						if(mb.getDepth() != 0){
							
						}else{
							out.println(mb.getNo());
						}
						
						%>
					</td>
					<td>
						<%
							if(mb.getDepth() > 0){
								out.println("<img height=1 width='" + mb.getDepth()*20 + "'>└> RE:");
							}else{
								
							}
						%>
						<a id="title" href="content.do?no=<%=mb.getNo()%>&view=<%=mb.getView() + 1%>"><strong><%=mb.getTitle()%></strong></a>
					</td>
					<td>
						<%
						if (mb.getWriter().equals("Anonymous")) {
							out.println("<em>" + mb.getWriter() + "</em>");
						} else {
							out.println(mb.getWriter());
						}
						%>
					</td>
					<td><%=mb.getDate()%></td>
					<td><%=mb.getView()%></td>
				</tr>
			</tbody>
			<%
			}
			%>
		</table>
		<br />
		<div id="pageNumberSector">
			<%
			MemberDAO dao = new MemberDAO();
			String searchText = request.getParameter("searchText");
			String option = request.getParameter("option");
			int allCount = dao.getCountFor(option, searchText);

			int list2 = 7;
			String nowPage = request.getParameter("page");
			int currentPage;
			if (nowPage != null) {
				currentPage = Integer.parseInt(nowPage);
			} else {
				currentPage = 1;
			}
			int blockCnt = 3;

			double blockNum1 = Math.ceil((double) currentPage / (double) blockCnt);
			int blockNum = (int) blockNum1;
			int blockStart = ((blockNum - 1) * blockCnt) + 1;
			int blockEnd = blockStart + blockCnt - 1;
			int pageStart = (currentPage - 1) * list2;
			double totalPage1 = Math.ceil((double) allCount / (double) list2);
			int totalPage = (int) totalPage1;
			if (blockEnd > totalPage) {
				blockEnd = totalPage;
			}

			if (allCount > 7) {

				if (currentPage > 1) {
					if (request.getParameter("searchText") == null) {
			%>
			<a href='list.do?page=1&pageStart=0'>처음...</a>
			<%
			} else {
			%>
			<a
				href='list.do?page=1&pageStart=0&option=<%=option%>&searchText=<%=searchText%>'>처음...</a>
			<%
			}
			} else {

			}
			if (currentPage <= 1) {

			} else {
			int prePage = currentPage - 1;
			if (request.getParameter("searchText") == null) {
			%>
			<a class=""
				href='list.do?page=<%=prePage%>&pageStart=<%=prePage * 7 - 7%>'>이전</a>
			<%
			} else {
			%>
			<a class="pagingBtn"
				href='list.do?page=<%=prePage%>&option=<%=option%>&searchText=<%=searchText%>&pageStart=<%=prePage * 7 - 7%>'>이전</a>
			<%
			}
			}
			for (int i = blockStart; i <= blockEnd; i++) {
			if (currentPage == i) {
			%>
			<h2 id="h2">
				<u><%=i%></u>
			</h2>
			<%
			} else {
			if (request.getParameter("searchText") == null) {
			%>
			<a class="pagingBtn"
				href="list.do?page=<%=i%>&pageStart=<%=i * list2 - 7%>"><%=i%></a>
			<%
			} else {
			%>
			<a class="pagingBtn"
				href="list.do?page=<%=i%>&option=<%=option%>&searchText=<%=searchText%>&pageStart=<%=i * list2 - 7%>"><%=i%></a>
			<%
			}
			}
			}
			if (currentPage >= totalPage) {

			} else {
			int nextPage = currentPage + 1;
			if (request.getParameter("searchText") == null) {
			%>
			<a class="pagingBtn"
				href="list.do?page=<%=nextPage%>&pageStart=<%=nextPage * 7 - 7%>">다음</a>
			<%
			} else {
			%>
			<a class="pagingBtn"
				href="list.do?page=<%=nextPage%>&option=<%=option%>&searchText=<%=searchText%>&pageStart=<%=nextPage * 7 - 7%>">다음</a>
			<%
			}
			}
			if (currentPage >= totalPage) {

			} else {
			if (request.getParameter("searchText") == null) {
			%>
			<a class="pagingBtn"
				href="list.do?page=<%=totalPage%>&pageStart=<%=totalPage * 7 - 7%>">...마지막</a>
			<%
			} else {
			%>
			<a class="pagingBtn"
				href="list.do?page=<%=totalPage%>&option=<%=option%>&searchText=<%=searchText%>&pageStart=<%=totalPage * 7 - 7%>">...마지막</a>
			<%
			}
			}

			} else {

			}
			%>
		</div>
		<div id="bottomSector">
			<button id="btn" onClick="location.href='write.do'">글쓰기</button>
		</div>
	</div>
</body>
</html>













'
