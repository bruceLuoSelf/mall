<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>错误页面</title>
	<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
	<style type='text/css'>
	.c1 {
		width: 900px;
		margin: 30px auto auto;
		background-color: #e99d06;
		font-size: 14px;
	}
	
	.c2 {
		background-color: #e99d06;
		color: #fff;
		height: 40px;
		line-height: 40px;
		font-weight: 700;
		font-size: 30px;
		text-align: center;
	}
	
	.c3 {
		height: auto !important;
		border-color: #e99d06;
		border: solid 1px #e99d06;
		background-color: #fff;
		color: #F00;
		text-align: left;
		padding: 10px 0px;
	}
	
	.c3 a {
		color: #00F;
		font-size: 15px;
		text-decoration: none;
	}
	
	.c3 a:hover {
		color: #00F;
	}
	
	.c3 p a {
		margin: 0px 10px;
		color: #F00;
		font-weight: 700;
	}
	
	p {
		height: 30px;
		margin: 0px;
		line-height: 22px;
	}
	
	.c4 {
		color: gray;
		padding-bottom: 10px;
		display: none;
	}
	
	.errorInfo {
		border-color: #e99d06;
		border: solid 1px #e99d06;
		margin: 0px 10px 10px 10px;
		font-size: 13px;
	}
	
	.errorMessage {
		margin: 0px 10px 10px 10px;
		font-size: 16px;
	}
	</style>
	<script>
		function showErrorInfo() {
			var errorInfo = document.getElementById('errorInfo');
			display = errorInfo.style.display;
			if (display == '' || display == 'none') {
				errorInfo.style.display = 'inline';
			} else {
				errorInfo.style.display = 'none';
			}
		}
	</script>
</head>
<body>
	<div class='c1'>
		<div class='c2'>错误信息</div>
		<div class='c3'>
			<p>
				<a href='javascript:' onclick='showErrorInfo()'>${ERROR}</a>
			</p>
			<div id='errorInfo' class='c4'>
				<h2 class='errorMessage'>错误详细信息</h2>
				<div class='errorInfo'>${stackTrace}</div>
			</div>
		</div>
	</div>
</body>
</html>