package com.qingruan.museum.msg.sensor;

import lombok.Data;

@Data
public class PatchInfo {
	private String patchKey;
	private Long timeStamp;
	private Long expires;
	private String ftpPath;
	private String ftpUser;
	private String ftpPassword;
	private int patchLength;
	private int totalPatchNumber;

}
