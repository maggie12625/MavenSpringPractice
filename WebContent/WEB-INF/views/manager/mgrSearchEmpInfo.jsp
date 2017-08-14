<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>主管查詢員工資料</title>
    <link rel="stylesheet" href="css/page.css" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
    <style>
        html{
            height: auto;
            width: auto;
            font-size: 20px!important;
        }
        body{
            height:100%;
            width:99%;
            text-align: center;
        }
        h1{
            margin: 15px 0;
        }
        table{
            width: 95%!important;
            margin:50px auto 0 auto!important;
            box-shadow:5px 5px 20px gray; 

            background-color: #ffffff;

        }
        form{
             margin:15px auto 0 auto;
        }
        tr{
            border:black 1px solid;
        }
        tr:first-child{
            background:black;
            color:white;

        }
        td{
            min-height:50px ; 
            min-width:90px ;
            outline:black 1px solid;
        }
        tr:first-child td{
            border-right: 1px solid white;

        }
        input{
            font-size: 1rem;
        }
        select{
            font-size: 1rem;
        }
        
}
    </style>
</head>
<body>
    <h1>查詢員工資料</h1>
	<hr>
	<form action='./Employee.do?action=search_employee' method="post">
		<select name='by'>
			<option value='name' ${name == conditions['name'] ? 'selected' : ''}>員工姓名</option>
			<option value='empno' ${empno == conditions['name'] ? 'selected' : ''}>員工編號</option>
		</select>
		<input type="text" name='keyword'>
		<input type="submit" value="查詢">
	</form>

	<table>
	 
		<tr>  
			<td>員編</td> 
			<td>員工姓名</td>
			<td>職位</td>
			<td>身分證</td>
			<td>email</td>
		</tr>
	      
              <%-- 透過 forEach 取出 list 內所有 DeptVO 物件 --%>
        <c:forEach var="employee" items="${empInfoList}">
           <tr>

                  <%-- 使用 EL 取出 deptVO 內所有屬性 --%>
                  <td>${employee.empno}</td>
                  <td>${employee.name}</td>
                  <td>${employee.position}</td>
                  <td>${employee.id}</td>
                  <td>${employee.email}</td>
           </tr>       
	    </c:forEach>
		
	</table>
	
    <%@ include file="/WEB-INF/views/changepage/page.jsp" %>
</body>
</html>