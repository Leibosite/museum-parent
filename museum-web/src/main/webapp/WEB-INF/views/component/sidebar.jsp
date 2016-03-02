<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/taglibs.jsp"%>

<ul class="nav nav-list">

	<c:forEach items="${subPermission}" var="s">
		<li> 
			<a href="${ctx}/${s.link}">
				<span class="navi-item">${s.displayname}</span>
				<span class="navi-item-sub">${s.desp}</span>
			</a>
			
		</li>
		<c:if test="${s.displayname == 'Access User' or s.displayname == 'Account Information' or s.displayname == 'Service Package'   }">
			<li class="divider"></li>
		</c:if>
	</c:forEach>
  <!-- <li> <a href="a"> <span class="navi-item">Period</span><span class="navi-item-sub">Set Period policy</span></a></li>
  <li class="divider"></li>
  <li><a href="a"> <span class="navi-item">Descriptor File</span><span class="navi-item-sub">Manage Descriptor file</span></a></li>
  <li><a href="a"> <span class="navi-item">PCC</span><span class="navi-item-sub">Manage PCC Rule</span></a></li>
  <li class="divider"></li>
  <li><a href="a"> <span class="navi-item">Threshold</span><span class="navi-item-sub">Build Threshold name list</span></a></li>
  <li><a href="a"> <span class="navi-item">Metering</span><span class="navi-item-sub">Manage Metering</span></a></li>
  <li><a href="a"> <span class="navi-item">Service Component</span><span class="navi-item-sub">Manage Service Component</span></a></li>
  <li><a href="a"> <span class="navi-item">User Profile set</span><span class="navi-item-sub">Manage User Profile</span></a></li> -->
</ul>
