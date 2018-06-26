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

<style> 
    #main {
	    width: 70%;
	    height: 600px;
	    margin: auto;
  	}
	input {
		width: 100%;
		height: 100%;
	}
   
</style> 
<%! 
	String result; 
	ArrayList<TLVObject> list; 
	boolean listCheck = false;
%> 
<% 

	
	if(request.getAttribute("listCheck") != null){
		list = (ArrayList<TLVObject>) request.getAttribute("TlvList"); 
	    listCheck = (boolean)request.getAttribute("listCheck"); 
	}	    
     
%> 

<%!
	  
	//여기서 point는 depth와 별개로 테이블의 열을 의미함 
	public String getTLV(ArrayList<TLVObject> tlvList, int depthOrg) { 
		TLVObject tlvObject; 
		String tag = ""; 
		String length = ""; 
		String stringValue = ""; 
		String result = ""; 
		int depth = depthOrg; 
		String depthTap = ""; 
		int rowspanTmp = 0; 
		int colspanTmp = depth + 2; 
	  
		result += "<table width='100%'>"; 
			/* result += "<table>";  */
		  
		result += "<tr>"; 
		  
		for (int i = 0; i < tlvList.size(); i++) { 
			tlvObject = tlvList.get(i); 
			tag = tlvObject.getTag(); 
			length = tlvObject.getLength(); 
			  
			//construct가 있다는 것 
			if (tlvObject.getValue() != null) { 
				result += "<td rowspan='2' valign=top>"; 
			} else { 
				result += "<td>"; 
			} 
			result += tag; 
			result += "</td>"; 
			  
			  
			//construct가 있다는 것 
			if (tlvObject.getValue() != null) { 
				// 뒤에 빈자리 합치기
				result += "<td colspan='2' >"; 
				result += length; 
				result += "</td>"; 
				//constructed일 때 한번 내려줘야함
				
				result += "</tr>";
				result += "<tr>";
				result += "<td colspan='2' >"; 
				result += getTLV(tlvObject.getValue(), depth + 1); 
			} else { 
				result += "<td>"; 
				result += length; 
				result += "</td>"; 
				  
				stringValue = tlvObject.getStringValue(); 
				result += "<td>"; 
				result += stringValue; 
				
			} 
			  
			result += "</td>"; 
			result += "</tr>"; 
		  
		} 
		
		result += "</table>"; 
		  
		return result; 
	}
%> 

<body>
	<table id="main"  width="auto" cellpadding="0" cellspacing="0" border="1" >
		<form action="" method="get">
			<tr>
				<td> HexaString </td>
				<td colspan="3"> <input type="text" name="hexaString" id="hexaString" value="${HexaString}"> </td>
			</tr>
			<tr id="result" >
				<td> Result </td>
				<td colspan="3">
				<div style="overflow:scroll; width:100%; height: 600px; padding:10px">
				<% if(listCheck){ %>
					<%out.println(getTLV(list, 0)); 
					} else {%>
					<pre>${Result}</pre>
				<% 	} %>
				</div>
				
				</td>
			</tr>
			<tr>
				<td> <input type="button" id="stringButton" class="button" value="Restful String 출력"></td>
				<td> <input type="button" id="listButton" class="button" value="Restful List 출력"></td>
				<td> <input type="button" id="stringButtonJar" class="button" value="Jar String 출력"></td>
				<td> <input type="button" id="listButtonJar" class="button" value="Jar List 출력"></td>
			</tr>
		</form>
	</table>
	
	<script>
		function clickHandler() {
			var btn = $('.button');
			for(var i = 0; i < btn.length; i++){
				btn[i].addEventListener("click", action);
			}
		}
	
		function action() {		
			switch (this.id) {
			case "stringButton":	
				location.href = "/tlvparser/json/string?hexaString="+$('#hexaString').val();
				break;
			case "listButton":	
				location.href = "/tlvparser/json/list?hexaString="+$('#hexaString').val();
				break;
			case "stringButtonJar":	
				location.href = "/tlvparser/jar/string?hexaString="+$('#hexaString').val();
				break;
			case "listButtonJar":	
				location.href = "/tlvparser/jar/list?hexaString="+$('#hexaString').val();
				break;
			}
			
			e.preventDefault();
		}
		

		window.addEventListener("load", clickHandler);
	</script>
</body>
</html>