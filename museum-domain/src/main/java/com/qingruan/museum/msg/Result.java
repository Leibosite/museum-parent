package com.qingruan.museum.msg;

import com.qingruan.museum.domain.enums.ResultCode;

public class Result {
	// 处理结果
	public ResultCode resultCode;
	// 排错描述信息
	public String troubleShootingMsg;

	public Result() {

	}

	public Result(ResultCode resultCode, String troubleShootingMsg) {
		this.resultCode = resultCode;
		this.troubleShootingMsg = troubleShootingMsg;

	}
}
