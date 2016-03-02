package com.qingruan.museum.engine.service.business.constantth.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.util.JSONUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.Device;
import com.qingruan.museum.dao.entity.record.ActivityConstantThData;
import com.qingruan.museum.domain.models.enums.RecordDataType;
import com.qingruan.museum.engine.service.business.constantth.HandleConstantThService;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.jpa.ActivityMonitoringDataService;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.constantth.ThDataContent;
import com.qingruan.museum.session.IpcanContext;

@Service
@Slf4j
public class HandleConstantThServiceImpl implements HandleConstantThService {
	@Autowired
	private ActivityMonitoringDataService activityMonitoringDataService;

	@Override
	public void saveConstantThDataReport(IpcanContext ipcanContext,
			RecordDataType recordDataType) {
		log.info("{Start HandleConstantThServiceImpl.saveConstantThDataReport()}");
		if (ipcanContext.getRecMuseumMsg() == null
				|| ipcanContext.getRecMuseumMsg().getMsgBody() == null)
			;
		else {

			if (ipcanContext.getRunTimeInfo().getApplicationType()
					.equals(ApplicationType.CONSTANT_TH)) {
				// 保存恒温恒湿机上报的数据
				this.saveConstantThData(ipcanContext.getRunTimeInfo()
						.getConstantThMsg(), ipcanContext);
			}

		}
		log.info("{End HandleConstantThServiceImpl.saveConstantThDataReport()}");
	}

	/**
	 * 保存恒温恒湿机上报的数据
	 * 
	 * @param msgBody
	 * @param ipcanContext
	 * @return
	 */
	private Boolean saveConstantThData(ConstantThMsg msg,
			IpcanContext ipcanContext) {
		if (msg != null && msg.getData() != null
				&& msg.getData().getDatas() != null) {

			List<ThDataContent> datas = msg.getData().getDatas();
			List<ActivityConstantThData> todoLists = new ArrayList<ActivityConstantThData>();
			for (ThDataContent content : datas) {
				ActivityConstantThData data = new ActivityConstantThData();

				Device device = ipcanContext.getRunTimeInfo().getConstantTh();
				if (device == null)
					return Boolean.FALSE;

				data.setDevice(device);
				data.setRepoArea(ipcanContext.getRunTimeInfo().getRepoArea());
				data.setObjectType(content.getMonitorDataType());

				data.setValue(content.getValue());
				data.setDateTime(new Timestamp(System.currentTimeMillis()));
				todoLists.add(data);

			}
			if (todoLists.size() != 0) {
				log.info("{start save constantTh data}-------[开始保存恒温恒湿机数据]");
				activityMonitoringDataService.saveAllConstantThData(todoLists);
				log.info("{end save constantTh data}-------[结束保存恒温恒湿机数据]"
						+ JSONUtil.serialize(todoLists));
				return Boolean.TRUE;
			}

		}

		return Boolean.FALSE;
	}

}
