<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message  key="pageTitle.pma_policygroup_editPolicy"/></title>

</head>
<body>
	<h2 style="position: relative">
		<c:if test="${empty policy.id}"><fmt:message key="th.createPolicy" /></c:if>
		<c:if test="${not empty policy.id}"><fmt:message key="th.edit" /> ${policy.policyName}</c:if>
	</h2>
	<hr>
	<!--include includes/form-rule-->
	<form:form id="js-pma-validate" class="pma-form" modelAttribute="policy"
		action="${ctx}/pma/policy/savePolicy?groupId=${groupId}&page=${page}" method="post">
		<c:if test="${not empty policy.id}">
			<form:hidden path="id" />
		</c:if>
		<c:if test="${not empty policy.status}">
			<form:hidden path="status" />
		</c:if>
			<input type ="hidden" name="policyGroup.id" value="${groupId}"/>
		<div class="well-small well well-bgwhite">
			<!--  just delete it 
			<blockquote>
				<h3>Tips:</h3>
				<p>Set up the policy.Just click !</p>
			</blockquote>
	 		-->
			<div class="form-inline">
				<div class="input-prepend">
					<span class="add-on"> <i class="icon-edit"></i>&nbsp;<fmt:message key ="th.policyName"/></span> 
					<form:input path="policyName" class="input-medium required" cssStyle="width:745px;"/>
				</div><fmt:message key="required"/>
				<div class="input-prepend">
					<span class="add-on"><i class="icon-circle-arrow-up"></i>&nbsp;<fmt:message key="th.precedence"/></span>
					<form:input path="salience" class="input-medium required digits"/>
				</div><fmt:message key="required"/>
			</div>
			<div style="padding-top:10px;"></div>
                <br/>
                <div class="input-prepend"><span class="add-on" style="height: 50px;"><i class="icon-tags"></i>&nbsp;规则描述</span>
              <form:textarea cssStyle="resize:none;width:1000px;height:50px;border-radius: 0 4px 4px 0;"   path="desp"/>
            </div>
			<hr>
			<div id="constraints-block"
				class="margin-top-bottom constraints-block">
				<h3 class="policy-title">
					策略触发条件<a id="add-constraint" href="#" data-toggle="tooltip"
						title="Add constraints"><i class="icon-plus-sign icon-white"></i></a>
				</h3>
			</div>
			<hr>
			<div id="executions-block" class="margin-top-bottom executions-block">
				<h3 class="policy-title">
					策略执行<a id="add-execution" href="#" data-toggle="tooltip"
						title="Add executions"><i class="icon-plus-sign icon-white"></i></a>
				</h3>
			</div>
		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-large btn-primary"><fmt:message key="btn.submit" /></button>
			<a class="btn btn-large" href="${ctx}/pma/policy/show/${groupId}?page=${page}"><fmt:message key="btn.cancel" /></a>
		</div>
	</form:form>
	<script type="text/javascript">
		window.ctx = "${ctx}";
		$(document).ready(function(){
			window.reservePolicyDisplay(${policyJson});
		});
		
	</script>
</body>