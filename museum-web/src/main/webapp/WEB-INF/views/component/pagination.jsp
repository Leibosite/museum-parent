<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="../common/taglibs.jsp"%>

<div class="js-pagination input-prepend input-append">
	<a data-action="first" class="first btn"> &lt;&lt; </a>
	<a data-action="previous" class="previous btn">&lt;</a>
	<input type="text" class="span" readonly="readonly"/>
	<a data-action="next" class="next btn">></a>
	<a data-action="last" class="last btn">>></a>
</div>

<div class="clearfix"></div>
<!-- Controller 里面的参数  pageUrl pageObjects -->
<script type="text/javascript">
	$(".js-pagination").jqPagination({
		
		current_page:${pageObjects.number+1},   
		max_page:${pageObjects.totalPages},
		paged:function(page){
			$("#js-ajax-repalce-block").disable({ 
		        cssClass: 'disabled', 
		        enableOnAjaxComplete: true, 
		        ajaxUrl: "${pageUrl}&page="+page, //对应Controller里面的page参数
		        ajaxCallback: function(data){ 
		            $("#js-ajax-repalce-block").html(data);
		     	}
		     })
		}
		
		
	});
</script>