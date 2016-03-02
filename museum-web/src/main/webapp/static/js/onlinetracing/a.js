(function() {
	$(function() {
		var e, t, n;
		$(".pre_disable_todo_block").disable({
			disableEveryElement : !0,
			maskClass : "js-disabled-mask-transparent"
		});
		n = $("#templateId").attr("value");
		e = $("#initialID").attr("value");
		$("ul.margin-top-bottom.task-list > li").each(function() {
			var t, r = this;
			t = $(this).attr("id");
			if (n) {
				if (t && e)
					return $.ajax({
						type : "GET",
						url : "show",
						data : {
							baseObjectId : e,
							resolveTarget : t,
							ajax : !0,
							templateId : n
						},
						success : function(e) {
							disable_restore();
							return addInfo_return(r, e)
						}
					})
			} else if (t && e)
				return $.ajax({
					type : "GET",
					url : "show",
					data : {
						baseObjectId : e,
						resolveTarget : t,
						ajax : !0
					},
					success : function(e) {
						disable_restore();
						return addInfo_return(r, e)
					}
				})
		});
		$(".constraints-content .dropdown-toggle").each(function() {
			var e;
			e = $(this);
			return e.find("a").click(function() {
				return e.html($(this).html() + " <span class='caret'></span>")
			})
		});
		$("[data-toggle='tooltip']").tooltip();
		t = '<div class="sub-attributes"><a href="#">Greater Than: &gt;</a><a href="#">Eqauls: =</a><a href="#">Less Than: &lt;</a></div>';
		return $("#opearator").popover({
			html : !0,
			content : t,
			trigger : "click"
		})
	});
	$.fn
			.extend({
				requestForm : function(e) {
					var t;
					t = {
						currentWrapper : "",
						ajaxUrl : "",
						afterRenderFunction : function() {
							return {}
						}
					};
					t = $.extend(t, e);
					return this
							.each(function() {
								var n = this;
								return $(this)
										.disable(
												{
													ajaxUrl : e.ajaxUrl,
													enableOnAjaxComplete : !0,
													ajaxData : {
														baseObjectId : $(
																"#initialID")
																.attr("value")
													},
													ajaxCallback : function(e) {
														var r;
														r = $(e);
														r.appendTo("body");
														r
																.modal("show")
																.on(
																		"hidden",
																		function() {
																			return r
																					.remove()
																		});
														r
																.find("form")
																.validate(
																		{
																			submitHandler : function(
																					e) {
																				return $(
																						e)
																						.ajaxSubmit(
																								{
																									type : "POST",
																									data : {
																										baseObjectId : $(
																												"#initialID")
																												.attr(
																														"value")
																									},
																									success : function(
																											e) {
																										addInfo_return(
																												n,
																												e);
																										disable_restore();
																										return r
																												.modal("hide")
																									}
																								})
																			}
																		});
														return t
																.afterRenderFunction()
													}
												})
							})
				}
			})
}).call(this);