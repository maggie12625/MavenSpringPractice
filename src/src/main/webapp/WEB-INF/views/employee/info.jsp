<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>個人資料</title>
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/person_info.css">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.js"></script>
<script src="js/person_info.js"></script>
</head>
<body>
	<div id='personInfo'>
		<div id='p_info'>
			<i id='close' class="fa fa-times-circle-o" aria-hidden="true"></i>
			<div id='title'>
				<h1>個人訊息</h1>
			</div>
			<div id='info_content'>
				<div id='div_table'>
					<table>
						<tr>
							<td class='bg_b_cl_w'>員工編號</td>
							<td colspan='2'><input id='id' type="text" value='${requestScope.employee.empno }'
								disabled='true'></td>
						</tr>
						<tr>
							<td class='bg_b_cl_w'>姓名</td>
							<td colspan='2'><input id='name' type="text" value='${requestScope.employee.name }'
								disabled='true'></td>
						</tr>
						<tr>
							<td class='bg_b_cl_w'>EMAIL</td>
							<td colspan='2'><input id='email' type='email'
								value='${requestScope.employee.email }' disabled='true'></td>
						</tr>
						<tr>
							<td id='state' rowspan='2' class='bg_b_cl_w'>職位</td>
							<td colspan='2'><span id='state_result' isGone='true'>${requestScope.employee.position }</span>
							</td>
						</tr>

					</table>
				</div>

			</div>

		</div>
	</div>

</body>
</html>