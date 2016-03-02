$.fn.extend
	timeHelper: (options) ->
		# Default settings
		settings =
			model:'day' # day *week month year *cron
			method:'bind' # bind remove
			path:'' # the path of form model attribute
		settings = $.extend settings,options
		fmt =
			dayDateForm :
				language:'zh-CN',
				startView:0,
				format:'hh:ii',
				minuteStep:10,
				maxView:1

			monthDateFmt :
				language:'zh-CN',
				startView:2,
				format:'mm月dd日',
				maxView:2,
				minView:2
			yearDateFmt :
				language:'zh-CN',
				startView:3,
				format:'mm月',
				maxView:3
				minView:3

		weekSelectBlock = """
			<select>
				<option value="1">星期一</option>
				<option value="2">星期二</option>
				<option value="3">星期三</option>
				<option value="4">星期四</option>
				<option value="5">星期五</option>
				<option value="6">星期六</option>
				<option value="7">星期日</option>
			</select>
		"""

		dateSelectBlock = """
			<input type="text" />
		"""

		cronSelectBlock = """
			<div><div style="display:inline-block"></div><input type="text" value=""/></div>
		"""


		# clearUp = (datePicker) ->
		# 	datePicker.css('display','inline-block')
		# 	datePicker.datetimepicker('remove')
		# 	datePicker.next().filter('.weekSelect').remove()
		copyprop = (dest,orig) ->
			attributes = orig.prop("attributes")
			$.each(attributes,()->
				if @.name != "type"
					dest.attr(@.name, @.value)
			)
			dest

		# @each ()->
		if settings.model == 'cron'
			cronSelect = $(cronSelectBlock)
			# cronSelect.attr('class',$(@).attr('class'))

			container = cronSelect.find('div')
			target = cronSelect.find('input')

			initialValue = if $(@).val()? && $(@).val()!="" then $(@).val() else "* * * * *"

			copyprop(target,$(@))

			$(@).replaceWith(cronSelect)
			
			container.cron({
					useGentleSelect: true
					initial: initialValue
					onChange: () ->
							target.val(container.cron('value'))
			})
			# target.hide()
			cronSelect

		else if settings.model == 'week'
			# clearUp($(@))
			weekSelect = $(weekSelectBlock)
			#weekSelect.attr($(@).attr())
			copyprop(weekSelect,$(@))
			$(@).replaceWith(weekSelect)
			weekSelect
			
		else
			dateSelect = $(dateSelectBlock)
			#dateSelect.attr($(@).attr())
			copyprop(dateSelect,$(@))
			$(dateSelect).datetimepicker(fmt[settings.model+'DateFmt'])
			$(@).replaceWith(dateSelect)
			dateSelect

$.fn.extend
	timeHelperChoose: (options) ->
		settings =
			dateOptions:["day","week","month","year","cron"]
		settings = $.extend settings,options
		timeHelperChooseStr = "<select><option value=''>请选择</option>"
		for option in settings.dateOptions
			timeHelperChooseStr += "<option value=#{option}>#{option}</option>"
		timeHelperChooseStr += "</select>"

		timeHelperChooseBlock = $(timeHelperChooseStr)
		$(@).before(timeHelperChooseBlock)
		target = $(@)
		timeHelperChooseBlock.bind('change', () ->
			target = target.timeHelper({model:timeHelperChooseBlock.val()})
		)
