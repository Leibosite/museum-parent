<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ include file="../common/taglibs.jsp"%>

<%-- <div style="padding: 0 20px" class="pull-left">
	<img src="${staticFile}/img/top-logo.png" class="top-logo" />
</div>  --%>
<div class="container">
	<!-- 标题 -->
	<h1 style="color: white;">甘肃省博物馆系统监控平台</h1>
	<!-- 导航条 -->
	<div class="btn-group">
		<button type="button" class="btn btn-large btn-default">
			<i class="icon-home"></i>
		</button>
		<button type="button"
			onclick="location.href='${ctx}/system/user/list'"
			class="btn btn-large btn-default">权限管理系统</button>
		<button type="button"
			onclick="location.href='${ctx}/pma/policygroup/list'"
			class="btn btn-large btn-default">监测与监控中心预警系统</button>
		<!-- 
		<button type="button"
			onclick="location.href='${ctx}/pma/policygroup/list'"
			class="btn btn-large btn-default">游客流量监测系统</button>
		<button type="button"
			onclick="location.href='${ctx}/pma/policygroup/list'"
			class="btn btn-large btn-default">数据可视化系统</button>
 -->
		<button type="button" onclick="location.href='${ctx}/logout'"
			class="btn btn-large btn-default">
			<i class="icon-off"></i>退出
		</button>
	</div>
</div>