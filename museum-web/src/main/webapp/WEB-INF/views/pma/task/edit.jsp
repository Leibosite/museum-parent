<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.pma_task_edit" /></title>

</head>
<body>
	<h2 style="position: relative">
		<c:if test="${action=='create'}">
			<fmt:message key="th.create" />
		</c:if>
		<c:if test="${action=='edit'}">
			<fmt:message key="th.edit" />
		</c:if>
		<fmt:message key="th.pma_task" />
	</h2>
	<hr />

	<form:form id="inputForm" modelAttribute="task"
		action="${ctx}/pma/task/save" method="post" class="form-horizontal">
		<c:if test="${not empty task.id}">
			<form:hidden path="id" />
		</c:if>
		<div class="well-small well well-bgwhite">
			<ul class="unstyled">
				<li><label><fmt:message key="pojo.Task.name" /></label> <form:input
						path="name" cssClass="required" /> <fmt:message key="required" /></li>
				<li><label><fmt:message key="pojo.Task.timmer" /> <fmt:message
							key="required" /></label>

			 <div class="timer"></div> <script type="text/javascript">
				 	$(function() {
							var inputValue = $('#timmer');
							var initialValue = (inputValue != null) && inputValue.val() != "" ? inputValue.val() : "* * * * * *";
							initialValue=initialValue.substring(2,initialValue.length).replace("?","*");
							$('.timer').cron(
									{
										useGentleSelect : true,
										initial : initialValue,
										onChange : function() {
											var time="* * * * * ?";
											var values=($('.timer').cron('value')).split(' ');
											if(values[4]=='*'&&values[2]=='*'){
												values[4]='?';
											}
											if(values[4]!='*'&&values[4]!='?'){
												values[2]='?';
											}
											if(values[2]!='*'&&values[2]!='?'){
												values[4]='?';
											}
											time="0 "+values.join(' ');
											return $('#timmer').val(time);
										}
									});
						}); 
					</script> <form:hidden path="timmer" cssClass="required" /></li>
				<li><label><fmt:message key="pojo.Task.status" /></label> <form:select
						path="status" cssClass="js-select required">
						<form:option value="">-Please Select-</form:option>
						<form:options items="${statusMap}" />
					</form:select> <fmt:message key="required" /></li>

				<%-- <form:hidden path="isForNoSubscribe" /> --%>
			</ul>
		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-large btn-primary">
				<fmt:message key="btn.submit" />
			</button>
			<a class="btn btn-large" href="${ctx}/pma/task/list?urlType=return"><fmt:message
					key="btn.cancel" /></a>
		</div>
	</form:form>
	<script type="text/javascript">
		$("#inputForm").validate( {
			errorElement : "span",
			rules : {
				timmer : {
					required : true,
					remote : {
						type : "post",
						url : "${ctx}/pma/task/cronCheck",
						data : {
							cronExpress : function() {
								return $("#timmer").val();
							}
						},
						dataType : "html",
						dataFilter : function(data, type) {
							if (data == "true") {
								return true;
							} else {
								return false;
							}
						}
					}
				}
			},
			success : function(label) {
				label.text("").addClass("success");
			},
			messages : {
				timmer : {
					remote : "定时器的cron表达式格式不正确。"
				}
			}
		} ); 

		function filterChange() {
			if ($("select[name='isForNoSubscribe']").val() == "1") {
				$('.noSubscribe').show();
				$('#sessionReleaseCause').addClass('required');
			} else {
				$('.noSubscribe').hide();
				$('#sessionReleaseCause').removeClass('required');
			}

		}

		 $(document).ready(function() {
			/* if ($("input[name='isForNoSubscribe']").val() == "1") {
				$("select[name='subUserFilter.id']").val("-1");
			} */
			filterChange();
		}) 
	</script>

</body>