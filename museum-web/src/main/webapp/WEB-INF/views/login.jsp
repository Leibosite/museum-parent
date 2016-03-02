<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.logging.SimpleFormatter"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<html>
<head>
<title>登录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
 
<link href="${staticFile}/login/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- font Awesome -->
<link href="${staticFile}/login/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<!-- Theme style -->
<link href="${staticFile}/login/css/AdminLTE.css" rel="stylesheet" type="text/css" />
</head>

<body class="bg">  
 <header>
	<div class="container">
        <div class="row">
              <div class="span12">
                  <span class="logo">甘肃省博物馆系统监控平台</span>
              </div>
        </div>
	</div>
 </header>
 <div class="container form-container">
		<div class="row">
			<div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-4 col-md-offset-4">
				<div class="panel panel-default panel-more-shadow">
					<div class="panel-body">
					<div class="panel-desc">登录</div>
						<hr>
						<form:form action="${ctx}/login" method="post" onsubmit="return logincheck()">
							<div class="form-group">
								<input type="text" id="username" value="${username}" name="username" class="form-control input-lg" placeholder="用户名" required autofocus>
							</div>
							<div class="form-group">
								<input type="password" id="password" name="password" class="form-control input-lg" placeholder="密码" required>
							</div>
							<div class="checkbox m-bot15">
								<label>
									<!-- <input type="checkbox"> 记住密码 -->
								</label>
							</div>
							<button type="submit" name="login" class="btn btn-lg btn-primary btn-block">登录</button>
							<div class="form-group m-top15">
								<a href="${ctx }/register">注册账号</a>
								<!-- <a href="javascript:;" class="pull-right">找回密码</a> -->
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>

<script LANGUAGE="javascript">
    function logincheck()
    {
        if(document.login.username.value.length==0){
            alert("请输入用户名");
            document.login.username.focus();
            return false;
        }

        if(document.login.password.value.length==0){
            alert("请输入密码!");
            document.login.password.focus();
            return false;
        }
    }
</script>
<!-- jQuery 2.0.2 -->
<script src="${staticFile}/login/js/jquery-2.0.3.min.js"></script>
<!-- Bootstrap -->
<script src="${staticFile}/login/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
