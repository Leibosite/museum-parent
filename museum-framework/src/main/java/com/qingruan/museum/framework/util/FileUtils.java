package com.qingruan.museum.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {
	
	private RandomAccessFile randomReadFile;
	private long fileSize;
	private int  packageNumber;
	
    
	public FileUtils(String filename) throws Exception{
		File f = new File(filename);
		this.fileSize = f.length();
		randomReadFile = new RandomAccessFile(f, "r");
		this.packageNumber = (int)(this.fileSize % 1000 > 0 ? (this.fileSize / 1000 + 1 ):(this.fileSize/1000));
	}
	
	public  byte[] readPackageData(int packageID){
		
		byte[] hexData = null;
		
		try {
			this.randomReadFile.seek((packageID-1)*1000);

			if(packageID<this.packageNumber){
				hexData = new byte[1000];
				randomReadFile.read(hexData, 0, 1000);
			}else {
				int lastDataSize = (int)(fileSize % 1000);
				hexData = new byte[lastDataSize];
				randomReadFile.read(hexData, 0, lastDataSize);
			}
		}catch (IOException e){
	            e.getStackTrace();
		} 
		
		return hexData;
	} 
	
	public void endFileOperate(){
		try {
			this.randomReadFile.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(int packageNumber) {
		this.packageNumber = packageNumber;
	}
	
	

	/*public static void main(String[] arge) throws Exception {

		
		File copyFile = new File("/Users/jcy/Desktop/copy_ble_app_beacon.hex");
		FileUtils util = new FileUtils("/Users/jcy/Desktop/ble_app_beacon.hex");
		System.out.println("包的总长度"+util.getFileSize());
		System.out.println("总包数"+util.getPackageNumber());
		FileOutputStream output = new FileOutputStream(copyFile);
		for (int i = 1; i <= 90; i++) {
			byte[] data = util.readPackageData(i);
			output.write(data);
		}
		output.flush();
		output.close();
		util.endFileOperate();
	}*/
}
