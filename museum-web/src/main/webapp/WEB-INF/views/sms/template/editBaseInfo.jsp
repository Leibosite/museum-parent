<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<div class="modal fade">
  <form:form id="inputForm" action="${ctx}/sms/template/save?ajax=true" style="margin:0px" modelAttribute="template">
  <div class="modal-header"><a data-dismiss="modal" class="close">×</a>
    <h3>
    <c:if test="${not empty template.id}"><fmt:message key="th.edit" /></c:if>
    <c:if test="${empty template.id}"><fmt:message key="th.create" /></c:if>
    <fmt:message key="sideNav.SMS_Template"/></h3>
  </div>
  <div class="modal-body">
  		<c:if test="${not empty template.id}">
			<form:hidden path="id" />
		</c:if>
		<div class="well-small well well-bgwhite">
			
			<!--h4.margin-top-bottom-small Input attributes-->
			<ul class="unstyled">
				<li>
					<label><fmt:message key="pojo.SmsTemplate.name" /></label> 
					<form:input path="name" maxlength="32"/>
				</li>
				<li>
					<label><fmt:message key="pojo.SmsTemplate.templateContent" /></label> 
					<form:textarea path="templateContent" rows="10" cols="50" cssClass="required"/><fmt:message key="required"/>
				</li>
				
			</ul>
		</div>
		
	
   
  </div>
  <div class="modal-footer">
  	<a data-dismiss="modal" class="btn"><fmt:message key="btn.close" /></a>
  	<button type="submit" class="btn btn-primary"><fmt:message key="btn.save" /></button>
  </div>
  </form:form>
</div>
