package com.qingruan.museum.api.repository;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springside.modules.test.spring.SpringTransactionalTestCase;

import com.qingruan.museum.dao.entity.record.HourDataRecord;
import com.qingruan.museum.dao.repository.record.HourDataRecordDao;
import com.qingruan.museum.framework.util.TimeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml","classpath*:/META-INF/spring/applicationContext-museum-*.xml"})

public class RealTimeTest {
	


	@Resource 
	private HourDataRecordDao hourDataRecordDao;
	
	private void getHourDataRecord(){
		Long startTime = TimeUtil.getLongTimeByFormat("2015-03-18 14:00", TimeUtil.DEFAULT_DATETIME_FORMAT_EX);
				
		Long endTime =   System.currentTimeMillis();
		Long areaId = 18L;
		List<HourDataRecord> hourDataRecords = hourDataRecordDao.findOneHourDataRecordByTimeScope(areaId, startTime, endTime);
		System.out.println(hourDataRecords);
	}

	@Test
	public void test(){
		getHourDataRecord();
	}
	/*@Autowired
	private RealTimeEvaluation realTimeEvaluation;
	@Autowired
	private StandardDao standardDao;
	@Autowired
	private RepoAreaDao repoAreaDao;
	
	@Test
	public void test(){
		//standardSaveDao();
		Standard s = standardDao.findOne(10L);
	}
	
	
	public void testFun() {
		IpcanContext ipcanContext = new IpcanContext();
		DomainBody domainBody = new DomainBody();
		MuseumDomain nuseumDomain = new MuseumDomain();
		RunTimeInfo runTimeInfo = new RunTimeInfo();

		runTimeInfo.setRepoArea(repoAreaDao.findOne(2L));
		domainBody.setSulfurDioxide(0.05);
		domainBody.setCarbonDioxide(20000.0);
		domainBody.setHumidity(30.0);
		domainBody.setTemperature(100.0);
		
		PmPojo pmPojo = new PmPojo();
		pmPojo.setPmType(PmType.PM_2_5);
		
		pmPojo.setValue(35.0);
		domainBody.setPmPojo(pmPojo);
		domainBody.setFormalclehyde(20.0);
		domainBody.setVoc(0.04);
		
		nuseumDomain.setDomainBody(domainBody);
		
		ipcanContext.setRecievedMuseumDomain(nuseumDomain);
		ipcanContext.setRunTimeInfo(runTimeInfo);
		
		
		realTimeEvaluation.doWithRealTimeData(ipcanContext);
		System.out.println(ipcanContext.getRealTimeScore());
	}
	@Test
	public void standardSaveDao()
	{
	
		StandardDomain domain = new StandardDomain();
		ExplainDomain explain = new ExplainDomain();
//standard	
		AtomicStandardDomain standardSO2 = new AtomicStandardDomain();	
		standardSO2.setMonitorObjectType(MonitorObjectType.SO2);
		standardSO2.setIdealValue(0.0);
		standardSO2.setIdealMin(0.05);
		standardSO2.setIdealMax(0.05);
		standardSO2.setAcceptMin(0.08);
		standardSO2.setAcceptMax(0.08);
		domain.getStandards().add(standardSO2);
		
		AtomicStandardDomain standardCO2 = new AtomicStandardDomain();	
		standardCO2.setMonitorObjectType(MonitorObjectType.CO2);
		standardCO2.setIdealValue(0.0);
		standardCO2.setIdealMin(14000.0);
		standardCO2.setIdealMax(14000.0);
		standardCO2.setAcceptMin(20000.0);
		standardCO2.setAcceptMax(20000.0);
		domain.getStandards().add(standardCO2);
		
		AtomicStandardDomain standardH = new AtomicStandardDomain();
		standardH.setMonitorObjectType(MonitorObjectType.HUMIDITY);
		standardH.setIdealValue(55.0);
		standardH.setIdealMin(50.0);
		standardH.setIdealMax(60.0);
		standardH.setAcceptMin(40.0);
		standardH.setAcceptMax(70.0);
		domain.getStandards().add(standardH);
		
		AtomicStandardDomain standardT = new AtomicStandardDomain();	
		standardT.setMonitorObjectType(MonitorObjectType.TEMPERATURE);
		standardT.setIdealValue(20.0);
		standardT.setIdealMin(18.0);
		standardT.setIdealMax(22.0);
		standardT.setAcceptMin(15.0);
		standardT.setAcceptMax(25.0);
		domain.getStandards().add(standardT);
		
		AtomicStandardDomain standardU = new AtomicStandardDomain();	
		standardU.setMonitorObjectType(MonitorObjectType.UV);
		standardU.setIdealValue(0.0);
		standardU.setIdealMin(10.0);
		standardU.setIdealMax(10.0);
		standardU.setAcceptMin(20.0);
		standardU.setAcceptMax(20.0);
		domain.getStandards().add(standardU);
		
		AtomicStandardDomain standardL = new AtomicStandardDomain();
		standardL.setMonitorObjectType(MonitorObjectType.LIGHTING);
		standardL.setIdealValue(50.0);
		standardL.setIdealMin(100.0);
		standardL.setIdealMax(100.0);
		standardL.setAcceptMin(200.0);
		standardL.setAcceptMax(200.0);
		domain.getStandards().add(standardL);
		
		
		AtomicStandardDomain standardV = new AtomicStandardDomain();	
		standardV.setMonitorObjectType(MonitorObjectType.TVOC);
		standardV.setIdealValue(0.0);
		standardV.setIdealMin(0.5);
		standardV.setIdealMax(0.5);
		standardV.setAcceptMin(0.8);
		standardV.setAcceptMax(0.8);
		domain.getStandards().add(standardV);
		
		AtomicStandardDomain standardPm25 = new AtomicStandardDomain();	
		standardPm25.setMonitorObjectType(MonitorObjectType.PM2_5);
		standardPm25.setIdealValue(0.0);
		standardPm25.setIdealMin(35.0);
		standardPm25.setIdealMax(35.0);
		standardPm25.setAcceptMin(75.0);
		standardPm25.setAcceptMax(75.0);
		domain.getStandards().add(standardPm25);
		
		AtomicStandardDomain standardPm10 = new AtomicStandardDomain();	
		standardPm10.setMonitorObjectType(MonitorObjectType.PM10);
		standardPm10.setIdealValue(0.0);
		standardPm10.setIdealMin(50.0);
		standardPm10.setIdealMax(50.0);
		standardPm10.setAcceptMin(150.0);
		standardPm10.setAcceptMax(150.0);
		domain.getStandards().add(standardPm10);
		
		AtomicStandardDomain standardF = new AtomicStandardDomain();	
		standardF.setMonitorObjectType(MonitorObjectType.CH4);
		standardF.setIdealValue(0.0);
		standardF.setIdealMin(50.0);
		standardF.setIdealMax(50.0);
		standardF.setAcceptMin(150.0);
		standardF.setAcceptMax(150.0);
		domain.getStandards().add(standardF);
		
		String json = JSONUtil.serialize(domain);
//explain		
		AtomicExplainDomain explaint=new AtomicExplainDomain();
		explaint.setMonitorObjectType(MonitorObjectType.TEMPERATURE);
		explaint.setIdealString("ideal");
		explaint.setLessString("less");
		explaint.setOverLeastString("overLeast");
		explaint.setMoreString("more");
		explaint.setOverMostString("overMost");
		explain.getExplains().add(explaint);
		
		AtomicExplainDomain explainh=new AtomicExplainDomain();
		explainh.setMonitorObjectType(MonitorObjectType.HUMIDITY);
		explainh.setIdealString("ideal");
		explainh.setLessString("less");
		explainh.setOverLeastString("overLest");
		explainh.setMoreString("more");
		explainh.setOverMostString("overMost");
		explain.getExplains().add(explainh);
		
		AtomicExplainDomain explainc=new AtomicExplainDomain();
		explainc.setMonitorObjectType(MonitorObjectType.CO2);
		explainc.setIdealString("ideal");
		explainc.setLessString("less");
		explainc.setOverLeastString("overLest");
		explainc.setMoreString("more");
		explainc.setOverMostString("overMost");
		explain.getExplains().add(explainc);
		
		AtomicExplainDomain explains=new AtomicExplainDomain();
		explains.setMonitorObjectType(MonitorObjectType.SO2);
		explains.setIdealString("ideal");
		explains.setLessString("less");
		explains.setOverLeastString("overLest");
		explains.setMoreString("more");
		explains.setOverMostString("overMost");
		explain.getExplains().add(explains);
		
		AtomicExplainDomain explainch=new AtomicExplainDomain();
		explainch.setMonitorObjectType(MonitorObjectType.CH4);
		explainch.setIdealString("ideal");
		explainch.setLessString("less");
		explainch.setOverLeastString("overLest");
		explainch.setMoreString("more");
		explainch.setOverMostString("overMost");
		explain.getExplains().add(explainch);
		
		AtomicExplainDomain explainp10=new AtomicExplainDomain();
		explainp10.setMonitorObjectType(MonitorObjectType.PM10);
		explainp10.setIdealString("ideal");
		explainp10.setLessString("less");
		explainp10.setOverLeastString("overLest");
		explainp10.setMoreString("more");
		explainp10.setOverMostString("overMost");
		explain.getExplains().add(explainp10);
		
		AtomicExplainDomain explainp25=new AtomicExplainDomain();
		explainp25.setMonitorObjectType(MonitorObjectType.PM2_5);
		explainp25.setIdealString("ideal");
		explainp25.setLessString("less");
		explainp25.setOverLeastString("overLest");
		explainp25.setMoreString("more");
		explainp25.setOverMostString("overMost");
		explain.getExplains().add(explainp25);
		
		AtomicExplainDomain explainu=new AtomicExplainDomain();
		explainu.setMonitorObjectType(MonitorObjectType.UV);
		explainu.setIdealString("ideal");
		explainu.setLessString("less");
		explainu.setOverLeastString("overLest");
		explainu.setMoreString("more");
		explainu.setOverMostString("overMost");
		explain.getExplains().add(explainu);
		
		AtomicExplainDomain explainl=new AtomicExplainDomain();
		explainl.setMonitorObjectType(MonitorObjectType.LIGHTING);
		explainl.setIdealString("ideal");
		explainl.setLessString("less");
		explainl.setOverLeastString("overLest");
		explainl.setMoreString("more");
		explainl.setOverMostString("overMost");
		explain.getExplains().add(explainl);
		
		AtomicExplainDomain explaintvo=new AtomicExplainDomain();
		explaintvo.setMonitorObjectType(MonitorObjectType.TVOC);
		explaintvo.setIdealString("ideal");
		explaintvo.setLessString("less");
		explaintvo.setOverLeastString("overLest");
		explaintvo.setMoreString("more");
		explaintvo.setOverMostString("overMost");
		explain.getExplains().add(explaintvo);
		
		String jsonExplain = JSONUtil.serialize(explain);
		
		Standard standard = new Standard();
		standard.setName("公司测试环境-软件部");		
		standard.setTimeStamp(new Date(System.currentTimeMillis()));
		standard.setTypeName("hh");
		standard.setDesp("hhhh");
		standard.setValue(json);
		standard.setExplain(jsonExplain);
		try {
			standardDao.save(standard);	
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}*/
}
