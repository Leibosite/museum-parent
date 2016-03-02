<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.pma_policygroup_edit"/></title>

</head>
<body>
    <h2 style="position: relative">
		<c:if test="${action=='create'}"><fmt:message key="th.create" /></c:if>
		<c:if test="${action=='edit'}"><fmt:message key="th.edit" /></c:if>
		<fmt:message key="pageTitle.pma_policygroup" />
	</h2>
	<hr />
	
	<form:form id="inputForm" modelAttribute="policyGroup"
		action="${ctx}/pma/policygroup/save" method="post" class="form-horizontal">
		<c:if test="${not empty policyGroup.id}">
			<form:hidden path="id" />
		</c:if>
		<div class="well-small well well-bgwhite">
			<div class="form-inline">
				<div class="input-prepend">
					<span class="add-on"> <i class="icon-edit"></i>&nbsp;<fmt:message key="pojo.PolicyGroup.groupName"/>
					</span> 
					 <form:input path="groupName" cssClass="required"  cssStyle="width:380px;"/>
				</div>
				<fmt:message key="required"/>
			</div>
			
			<div style="padding-top:10px;"></div>
                <br/>
                <div class=" input-prepend "><span class="add-on input-group-addon" ><i class="icon-tags"></i>&nbsp;规则描述&nbsp;&nbsp;&nbsp;</span>
                 <form:textarea cssStyle="resize:none;width:380px;height:80px;"   path="desp"/>
            </div>
            
		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-large btn-primary"><fmt:message key="btn.submit" /></button>
			<a class="btn btn-large" href="${ctx}/pma/policygroup/list?urlType=return"><fmt:message key="btn.cancel" /></a>
		</div>
	</form:form>
	<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>