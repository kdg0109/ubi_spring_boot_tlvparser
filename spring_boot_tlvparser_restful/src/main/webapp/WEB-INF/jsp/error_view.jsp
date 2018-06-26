<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%> 
<%@ page import="java.util.ArrayList" import="tlvparser.TLVObject" %> 
<%@ taglib prefix="c" uri= "http://java.sun.com/jsp/jstl/core" %> 
    <!-- autocomplete from jQuery Ui -->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Input HexaString</title>
</head>
<body>
	${msg} 입니다.
	<br>
	<input type="button" id="returnButton" class="button" value="되돌아 가기">
</body>
	<script>
		function clickHandler() {
			var btn = $('.button');
			for(var i = 0; i < btn.length; i++){
				btn[i].addEventListener("click", action);
			}
		}
	
		function action() {		
			switch (this.id) {
			case "returnButton":	
				location.href = "http://localhost:8085/convert";
				break;
			}
			
			e.preventDefault();
		}
		

		window.addEventListener("load", clickHandler);
	</script>
</html>