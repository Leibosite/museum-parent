#common function

$ ->
	$(".pre_disable_todo_block").disable({
		disableEveryElement:true
		,maskClass:'js-disabled-mask-transparent'
	})
	templateId = $("#templateId").attr('value');
	baseObjectId = $("#initialID").attr('value');
	$("ul.margin-top-bottom.task-list > li").each -> 
		resolveTarget = $(@).attr('id')
		if templateId
			if resolveTarget && baseObjectId
				$.ajax
					type:'GET',
					url:'show',
					data: baseObjectId:baseObjectId, resolveTarget:resolveTarget, ajax:true, templateId:templateId
					success:(data) =>
						disable_restore()
						addInfo_return @,data
		else
			if resolveTarget && baseObjectId
				$.ajax
					type:'GET',
					url:'show',
					data: baseObjectId:baseObjectId, resolveTarget:resolveTarget, ajax:true
					success:(data) =>
						disable_restore()
						addInfo_return @,data
	
	# init all the selections
	$(".constraints-content").each ->
		$(@).ruleOption()

	$(".js-close-btn").click ->
		$(@).parent().remove()

operatorBlockBuilder = (operators) -> 
	string_buffer = ""
	operator = for operator in operators
		string_buffer += """<li><a href="#"> """ + operator+"""</a></li>"""
	result = 	"""
							<div class="sub-attributes">
								#{string_buffer}
							</div>
						"""
#rule Option plugin
$.fn.extend
	ruleOption: (options) ->
		content = $(@).find("#constraints-element button.dropdown-toggle").get(0);
		_this = @
		$(@).find("ul li a").click ->

			#remove the previous blocks
			$("#operator").remove()
			$("#inputBlock").remove()

			title = $(@).html()
			type = $(@).attr("pma-oper-type")
			$(content).html(title + "<span class='caret'></span>")
			# $(content).dropdown("toggle")
			input_c = $(_this).find("[name='constraint-type']").get(0);
			$(input_c).val(title)
			
			#build the operator block
			resultHtml = switch type
				when "LONG" then operatorBlockBuilder(["GREATER","LESS","adasdfas"])
				when "STRING" then operatorBlockBuilder(["GREATER","LESS"])
				when "OBJECT" then operatorBlockBuilder(["GREATER","LESS"])
				when "INTEGER" then operatorBlockBuilder(["GREATER","LESS"])
				when "ENUM" then operatorBlockBuilder(["GREATER","LESS"])
			
			operatorBlock = """
				<div style="display:inline-block" id="operator" class="btn-group">
					<button data-toggle="dropdown" class="btn btn-mini btn-warning dropdown-toggle"><i class="icon-white icon-hand-up"></i><span class="caret"></span>
						<input type="hidden" name="operator-type" style="display:none">
					</button>
					<ul class="dropdown-menu">
						#{resultHtml}
					</ul>
				</div>
			"""
			
			operatorBlock = $(operatorBlock).appendTo _this
			operator = operatorBlock.find("button.dropdown-toggle").get(0);
			$(operatorBlock).find("ul li a").click ->
				title = $(@).html()
				$(operator).find("i").remove()
				$(operator).prepend("<i>"+title+"</i>")
				$(operator).dropdown("toggle")
				false
			

			inputBlock = """
					input.input-small(id="inputBlock" type="text",style="display:inline-block;position:relative;margin:0 4px;",placeholder="Set value")
				"""
			$(inputBlock).appendTo _this
			false


#requestForm-plugin
$.fn.extend
	requestForm: (options) ->
		# Default settings
		settings =
			currentWrapper:''
			ajaxUrl:''
			afterRenderFunction: -> {}

		# Merge default settings with options.
		settings = $.extend settings, options

		return @each ()->
			$(@).disable(

				ajaxUrl:options.ajaxUrl
				enableOnAjaxComplete:true
				ajaxData: baseObjectId:$("#initialID").attr('value')
				ajaxCallback: (data) => 
					$page = $(data)
					$page.appendTo("body")
					$page.modal('show').on('hidden', -> $page.remove())
					
					#add datepicker
					#$page.find('.js-datepicker').datepicker({format: 'mm-dd-yyyy'});

					$page.find('form').validate(
						submitHandler:(form) => 
							$(form).ajaxSubmit(
								type:'POST',
								data: baseObjectId:$("#initialID").attr('value')
								success: (data)=>
									addInfo_return @,data
									disable_restore()
									$page.modal('hide')
							)		
					)
					settings.afterRenderFunction()

			)


#simple request form. 为了简单列表单页的提交修改
$.fn.extend
	simpleRequestForm: (options) ->
		# Default settings
		settings =
			currentWrapper:''
			ajaxUrl:''
			submitCallBack: (data) -> return window.location.reload()
			addRules: -> {}
			afterRenderFunction: -> {}
			
		# Merge default settings with options.
		settings = $.extend settings, options

		return @each ()->
			$(@).disable(

				ajaxUrl:options.ajaxUrl
				enableOnAjaxComplete:true
				ajaxCallback: (data) => 
					$page = $(data)
					$page.appendTo("body")
					$page.modal('show').on('hidden', -> $page.remove())
					
					#add datepicker
					#$page.find('.js-datepicker').datepicker({format: 'mm-dd-yyyy'});

					$page.find('form').validate(
						submitHandler:(form) => 
							$(form).ajaxSubmit(
								type:'POST',
								success: (data)=>
									# addInfo_return @,data
									disable_restore()
									settings.submitCallBack(data)
									$page.modal('hide')
							)		
					)
					settings.addRules()
					settings.afterRenderFunction()

			)














