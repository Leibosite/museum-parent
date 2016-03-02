function addInfo_return(target,data){
	$(target).addClass('ok');
	$(target).find('.oper-block-list.btn-group,.btn-group.group-wrapper').remove();
	$(target).find('.complete_items').remove();
	$(target).append(data);
}
	
function disable_restore(){
	$('.pre_disable_todo_block').disable_restore({maskClass:'js-disabled-mask-transparent'});
}
