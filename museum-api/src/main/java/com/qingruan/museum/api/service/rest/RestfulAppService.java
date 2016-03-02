package com.qingruan.museum.api.service.rest;

public interface RestfulAppService {
	
	 String fetchMuseumOverviewData();
	 String fetchShowroomDetailData(int showroomID);
	 String fetchMonitorDataByObjectType(int objectType,int showroomId);
}
