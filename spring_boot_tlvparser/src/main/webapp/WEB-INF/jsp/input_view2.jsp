<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%> 
<%@ page import="java.util.ArrayList" import="tlvparser.TLVObject" %> 
<%@ taglib prefix="c" uri= "http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>Input HexaString</title> 
</head> 
  
<style> 
    #main {
	    width: 70%;
	    height: 2000px;
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
%> 
<% 
	list = (ArrayList<TLVObject>) request.getAttribute("TlvList"); 
     
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
	<table id="main" width="auto" cellpadding="0" cellspacing="0" border="1" >
		<form action="convert.do" method="post">
			<tr>
				<td> HexaString </td>
				<td> <input type="text" name="HexaString" value="${HexaString}"> </td>
			</tr>
			<tr id="result" >
				<td> Result </td>
				<td>
					<div style="overflow:scroll; width:100%; height: 2000px; padding:10px"><% out.println(getTLV(list, 0)); %> <div>
				</td>
			</tr>
			<tr>
				<td colspan="2"> <input type="submit" value="변환"></td>
			</tr>
		</form>
	</table>
</body> 
</html>
