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
	margin: 0 auto;
	text-align: center;
	height: 430px;
}
#inputContent{
	width: 80%;
	height: 200px;
}
#submit{
	float: right;
}
#bottomSector{
	width: 100%;
}
#btn{
	float: right;
}
</style>
</head>
<body>
	<div id="page">
		<h1>This is write Page</h1>
		<hr />
		<form action="insert" method="post" id="form" enctype="multipart/form-data">
			작성자 : <input type="text" name="inputWriter"><br />
			제목 : <input type="text" name="inputTitle"><br />
			내용 : <textarea id="inputContent" name="inputContents"></textarea><br /> 
			첨부파일 : <input type="file" name="file">
			<input id="submit" type="submit" value="등록">
		</form>
		<div id="bottomSector"><button id="btn" onclick="location.href='list.do'">목록</button></div>
	</div>
</body>
</html>