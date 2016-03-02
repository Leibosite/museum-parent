<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="bg-black">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
<%@ include file="common/scripts.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
 
<link href="${staticFile}/login/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- font Awesome -->
<link href="${staticFile}/login/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<!-- Theme style -->
<link href="${staticFile}/login/css/AdminLTE.css" rel="stylesheet" type="text/css" />
 
</head>
<body class="bg">
<div class="form-box" id="login-box">
    <div class="header">注册</div>
    <form:form action="${ctx}/register/save" method="post" onsubmit="return regcheck()" >
        <div class="body bg-gray">
            <div class="form-group">
                <input path="loginName" id="loginName" type="text" name="loginName" class="form-control" placeholder="登陆名">
            </div>
            
            <div class="form-group">
                <input path="name" type="text" id="name" name="name" class="form-control" placeholder="用户名">
            </div>

            <div class="form-group">
                <input path="password" type="password" id="password" class="form-control" name="password" placeholder="密码" >
            </div>

            <div class="form-group">
                <input path="password" type="password" id="againPassword" class="form-control" name="repassword" placeholder="重复密码" >
            </div> 
            <div class="form-group">
                <input path="email" type="text" name="email" id="email" class="form-control" placeholder="邮箱" >
            </div> 
            <div class="form-group">
                 <input path="state" name="state" id="state"  style="display: none" type="text" class="form-control" value="0" >
            </div> 
        </div>
        <div class="footer">

            <button type="submit" class="btn bg-olive btn-block">注册</button>

            <a href="login" class="text-center">已经注册？请登录</a>
        </div>
    </form:form>

   <!-- <div class="margin text-center">
        <span>下面的按钮暂时是不可用的</span>
        <br/>
        <button class="btn bg-light-blue btn-circle"><i class="fa fa-facebook"></i></button>
        <button class="btn bg-aqua btn-circle"><i class="fa fa-twitter"></i></button>
        <button class="btn bg-red btn-circle"><i class="fa fa-google-plus"></i></button>

    </div>-->
</div>

<!-- jQuery 2.0.2 -->
<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script> -->
<!-- Bootstrap -->
<script src="${staticFile}/login/js/bootstrap.min.js" type="text/javascript"></script>

<script LANGUAGE="javascript">
	$(function(){
		$("#loginName").blur(function(){
			$.ajax({
				type : 'POST',
				url : "register/checkUser",
				data : {
					loginName : $('#loginName').val()
				},
				success : function(msg) {
					if (msg == 1) {
						alert("您输入的用户名存在！请重新输入！");
					}
				}
			})
		})
	})
    function regcheck()
    { 
        if($("#loginName").val()==""){
            alert("请输入登陆名");
            $("#loginName").focus();
            return false;
        }
        if($("#name").val()==""){
            alert("请输入用户名");
            $("#name").focus();
            return false;
        }
		
        if($("#name").val().length < 6){
            alert("用户名长度至少6位");
            $("#name").focus();
            return false;
        }

        if($("#password").val()==""){
            alert("请输入密码!");
            $("#password").focus();
            return false;
        }

        if($("#password").val().length < 8){
            alert("密码长度至少8位!");
            $("#password").focus();
            return false;
        }

        if($("#againPassword").val()==""){
            alert("请重复输入密码!");
            $("#againPassword").focus();
            return false;
        }
        if($("#againPassword").val() != $("#password").val()){
            alert("两次密码输入不符!");
            $("#againPassword").focus();
            return false;
        }


        if($("#email").val()==""){
            alert("请输入邮箱!");
            $("#email").focus();
            return false;
        }
 


        //var temp = document.getElementById("text1");
        //var email = document.getElementById("email");

        //对电子邮件的验证
        //定义email正则表达式
        var email_reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;

        if(!email_reg.test($("#email").val())) {
            alert('提示\n\n请输入有效的E-mail！');
            $("#email").focus();
            return false;
        }

        // 特殊字符检验
        var no_allow_txt = new RegExp("[\\*,\\&,\\\\,\\/,\\?,\\|,\\:,\\<,\\>,\"]");
        if(no_allow_txt.test($("#loginName").val())){
            alert("用户名不允许含特殊字符");
            $("#loginName").focus();
            return false;
        }
        alert("申请成功！")
        return true;
    }

</script>
</body>
</html>