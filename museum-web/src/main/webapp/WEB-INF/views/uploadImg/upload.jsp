<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试上传图片</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>
<script type="text/javascript" src="${staticFile}/js/jquery.min.js"></script>
<link type="text/css" rel="stylesheet" href="${staticFile}/js/uploadify/uploadify.css">
<script type="text/javascript" src="${staticFile}/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#file_upload")
								.uploadify(
										{
											'buttonText' : '请选择',
											'height' : 30,
											'width' : 120,
											'swf' : '${staticFile}/js/uploadify/uploadify.swf',
											'uploader' : '${pageContext.request.contextPath}/uploadify/uploadFile',
											'auto' : false,
											'fileDesc' : 'png文件或jpg文件或gif文件',
											'fileExt' : '*.png;*.jpg;*.gif',
											'fileObjName' : 'file',
											'onUploadSuccess' : function(file,
													data, response) {
												alert(file.name + ' 上传成功！ ');
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
				<h5>历史告警</h5>
			</div>
			<div class="widget-content" style="height: 500px;">
				<input type="file" name="fileName" id="file_upload" /> <a
					href="javascript:$('#file_upload').uploadify('upload', '*')">上传文件</a>
				| <a href="javascript:$('#file_upload').uploadify('stop')">停止上传!</a>
			</div>
		</div>
	</div>
</body>
</html>