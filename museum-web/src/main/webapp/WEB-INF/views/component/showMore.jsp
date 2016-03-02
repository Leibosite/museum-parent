<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/taglibs.jsp"%>

<a class="btn" id="showMore">Show more...</a>

<script type="text/javascript">
	$("#showMore").click(function(){
		$("#js-ajax-repalce-block").disable({ 
		  cssClass: 'disabled', 
		  enableOnAjaxComplete: true, 
		  ajaxUrl: "${pageUrl}&page="+${pageObjects.number+1},
		  ajaxCallback: function(data){
		      $("#js-ajax-repalce-block").html(data);
		  }
		});	
	});
	
</script>