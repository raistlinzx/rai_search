<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/common/common.jsp"%>
<%
	String method = request.getParameter("method");
	List<DBObject> result = null;
	if ("insert".equals(method)) {		
		String content = request.getParameter("content");
		if (content != null && !"".equals(content))
			SearchFactory.initDocument(content);
	} else {
		String keyword = request.getParameter("keyword");
		if (keyword != null && !"".equals(keyword))
			result = SearchFactory.search(keyword);

	}
%>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<form action="?method=insert" method="post">
	insert document:
	<br />
	<textarea name="content" rows="5" cols="40"></textarea>
	<br />
	<input type="submit" value="insert" />
</form>
<hr />
<form action="?method=search" method="post">
	search:
	<input name="keyword" />
	<input type="submit" value="search" />
</form>
<%
	if (result != null && result.size() > 0) {
%>
<table>
	<%
		for (DBObject o : result) {
	%>
	<tr>
		<td><%=o.get("content")%></td>
	</tr>
	<%
		}
	%>
</table>
<%
	}
%>
</body>
</html>