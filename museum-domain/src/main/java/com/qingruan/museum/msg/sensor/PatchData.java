package com.qingruan.museum.msg.sensor;

import lombok.Data;

@Data
public class PatchData {
	private int idNumber;
	private int totalPackageNumber;
	private int packageSize;
	private byte[] upgradeData;
}
