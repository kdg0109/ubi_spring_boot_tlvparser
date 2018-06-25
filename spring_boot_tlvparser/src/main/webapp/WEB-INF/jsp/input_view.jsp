<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri= "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Input HexaString</title>
</head>

<style>
  table, th, td {
    border: 1px solid #bcbcbc;
  }
  table {
    width: 70%;
    height: 400px;
    margin: auto;
  }
  input {
    width: 100%;
    height: 100%;
  }
  
</style>

<body>
	<table width="500" cellpadding="0" cellspacing="0" border="1" >
		<form action="convert.do" method="post">
			<tr>
				<td> HexaString </td>
				<td> <input type="text" name="HexaString"> </td>
			</tr>
			<tr id="result" >
				<td> Result </td>
				<td>
					<div style="overflow:scroll; width:100%; height: 200px; padding:10px"><pre></pre><div>
				</td>
			</tr>
			<tr>
				<td colspan="2"> <input type="submit" value="변환"></a></td>
			</tr>
		</form>
	</table>
</body>
</html>