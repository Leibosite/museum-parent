<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>规则配置</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type : "Post",
			dataType : "JSON",
			url : '${pageContext.request.contextPath}/ruleConfigList/showContent',
			async : false,
			success:function(msg){
				$("#ckeditor_full").val(msg);
			}
		}); 
	});
</script>
</head>
<body>
	<div class="container">
		<div class="widget-box">
			<div class="widget-title">
				<span class="icon"> <i class="icon-bookmark"></i></span>
				<h5>规则配置</h5>
			</div>
			<div class="widget-content" style="height: 500px;">
				<form action="save" method="post" style="width: 100%;height: 100%">
					<textarea id="ckeditor_full" name="drl" style="width:99%; height:90%;"></textarea>
					<P style="text-align: right;">
						<input type="submit" class="btn btn-primary" value="保存">&nbsp&nbsp
						<input type="button" onclick="location.href='list'" value="返回" class="btn btn-warning">
					</P>
				</form>
			</div>
		</div>
	</div>
</body>
</html>