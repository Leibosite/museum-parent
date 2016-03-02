package com.qingruan.museum.netty.task;


public class FixTimerTask  {

	private String masterGetwayid;

	public FixTimerTask(String masterGetwayid) {
		this.masterGetwayid = masterGetwayid;
	}

	
	public String getMasterGetwayid() {
		return masterGetwayid;
	}

	public void setMasterGetwayid(String masterGetwayid) {
		this.masterGetwayid = masterGetwayid;
	}

//	public void initTask() {
//
//		// 重置接收到的82的数量 准备重新发送
//		CommonData.recev82PathNum.put(masterGetwayid, null);
//		// 剔除掉sequenceListMap中成功的 把在sequenceSuccessListMap的路径剔除掉
//		int size = CommonData.sequenceListMap.get(masterGetwayid).size();
//		ArrayList<String> arrayList = CommonData.sequenceSuccessListMap
//				.get(masterGetwayid);
//		String[] pathArray = new String[size];
//		for (int i = 0; i < size; i++) {
//			String string = CommonData.sequenceListMap.get(masterGetwayid).get(
//					i);
//			pathArray[i] = string;
//		}
//		// 剔除掉sequenceListMap中成功的
//		for (int i = 0; i < pathArray.length; i++) {
//			for (int j = 0; j < arrayList.size(); j++) {
//				if (pathArray[i].equals(arrayList.get(j))) {
//					CommonData.sequenceListMap.get(masterGetwayid).remove(i);
//				}
//			}
//		}
//	}
//	public void init() {
//
//		// 重置接收到的82的数量 准备重新发送
//		CommonData.recev82PathNum.put(masterGetwayid, null);
////		CommonData.sequenceSuccessListMap.put(masterGetwayid, null);
//		
//	}

}
