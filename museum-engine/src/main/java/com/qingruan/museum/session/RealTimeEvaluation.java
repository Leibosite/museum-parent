//package com.qingruan.museum.session;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.qingruan.museum.dao.entity.CustomizedStandard;
//import com.qingruan.museum.dao.entity.RepoArea;
//import com.qingruan.museum.dao.entity.Standard;
//import com.qingruan.museum.dao.repository.CustomizedStandardDao;
//import com.qingruan.museum.dao.repository.RepoAreaDao;
//import com.qingruan.museum.dao.repository.StandardDao;
//import com.qingruan.museum.domain.enums.MonitorObjectType;
//import com.qingruan.museum.domain.model.standard.AtomicExplainDomain;
//import com.qingruan.museum.domain.model.standard.AtomicStandardDomain;
//import com.qingruan.museum.domain.model.standard.ExplainDomain;
//import com.qingruan.museum.domain.model.standard.StandardDomain;
//import com.qingruan.museum.domain.models.pojo.SocreAndExplainPojo;
//import com.qingruan.museum.framework.jackson.JSONUtil;
//
///**
// * 实时环境的数据进行实时评分
// * 
//* @author leibosite
// *
// */
//@Service
//public class RealTimeEvaluation {
//
//	private final static Double IDEALINITIALSCORE = 8.0;
//	private final static Double ACCEPTINITIALSCORE = 6.0;
//	private final static Double IDEALDEVIATIONSCORE = 2.0;
//	private final static Double ACCEPTDEVIATIONSCORE = 2.0;
//	private final static Double NOTWARNINGSCORE = 4.0;
//	private final static Double WRININTSCORE = 6.0;
//
//	@Autowired
//	private CustomizedStandardDao customizedStandardDao;
//	@Autowired
//	private StandardDao standardDao;
//	@Autowired
//	private RepoAreaDao areaDao;
//
//	/**
//	 * 处理警告范围
//	 */
//	private Double getWarningRangScore(Double currentValue, Double idealValue,
//			Double acceptMin, Double acceptMax) {
//		Double score = null;
//		if (currentValue <= acceptMin) {
//			score = NOTWARNINGSCORE / Math.abs(acceptMin - idealValue)
//					* Math.abs(currentValue - acceptMin);
//			return score < WRININTSCORE ? score : WRININTSCORE;
//		} else {
//			score = NOTWARNINGSCORE / Math.abs(acceptMax - idealValue)
//					* Math.abs(currentValue - acceptMax);
//			return score < WRININTSCORE ? score : WRININTSCORE;
//		}
//	}
//
//	/**
//	 * 处理理想范围分数 返回当前分数
//	 */
//	private Double getIdealRangeScore(Double currentValue, Double idealValue,
//			Double MinOrMaxValue) {
//		if (currentValue == idealValue)
//			return IDEALINITIALSCORE + IDEALDEVIATIONSCORE;
//		return IDEALINITIALSCORE
//				+ (Math.abs(MinOrMaxValue - idealValue) / Math.abs(currentValue
//						- idealValue)) * IDEALDEVIATIONSCORE;
//	}
//
//	/**
//	 * 处理可接受范围 返回当前分数
//	 */
//	private Double getAcceptRangeScore(Double currentValue,
//			Double idealMinOrMaxValue, Double acceptMinOrMaxValue) {
//		return ACCEPTINITIALSCORE
//				+ (Math.abs(currentValue - idealMinOrMaxValue) / Math
//						.abs(acceptMinOrMaxValue - idealMinOrMaxValue))
//				* ACCEPTDEVIATIONSCORE;
//	}
//
//	private void computeSO2(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString) {
//		
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.SO2);
//		Double sulfurDioxide = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getSulfurDioxide();
//		// 当前SO2值是否在理想范围里
//		// dealWithCurrentValue(sulfurDioxide,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (sulfurDioxide >= idealMin && sulfurDioxide <= idealMax) {			
//						
//			scoreAndExplain.setExplain(idealString);
//			if (sulfurDioxide <= idealValue) {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(sulfurDioxide, idealValue,
//										idealMin));
//			} else {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(sulfurDioxide, idealValue,
//										idealMax));
//			}
//		}
//		// 是否在可接受范围里
//		else if (sulfurDioxide >= acceptMin && sulfurDioxide <= acceptMax) {
//			if (sulfurDioxide <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(sulfurDioxide, idealMin,
//										acceptMin));
//			} else if (sulfurDioxide >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(sulfurDioxide, idealMax,
//										acceptMax));
//			}
//		} else if (sulfurDioxide < acceptMin || sulfurDioxide > acceptMax) {
//			if (sulfurDioxide < acceptMin)
//				scoreAndExplain.setExplain(overLeastString);
//			else
//				scoreAndExplain.setExplain(overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(sulfurDioxide, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//		
//	}
//
//	private void computeCO2(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString) {
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.CO2);
//		Double carbonDioxide = ipcanContext.getRecievedMuseumDomain()		
//				.getDomainBody().getCarbonDioxide();
//		// dealWithCurrentValue(carbonDioxide,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (carbonDioxide >= idealMin && carbonDioxide <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (carbonDioxide <= idealValue) {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(carbonDioxide, idealValue,
//										idealMin));
//			} else {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(carbonDioxide, idealValue,
//										idealMax));
//			}
//		} else if (carbonDioxide >= acceptMin && carbonDioxide <= acceptMax) {
//			if (carbonDioxide <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(carbonDioxide, idealMin,
//										acceptMin));
//			} else if (carbonDioxide >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(carbonDioxide, idealMax,
//										acceptMax));
//			}
//		} else if (carbonDioxide < acceptMin || carbonDioxide > acceptMax) {
//			if (carbonDioxide < acceptMin)
//				scoreAndExplain.setExplain(overLeastString);
//			else
//				scoreAndExplain.setExplain(overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(carbonDioxide, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//
//	private void computeTemperature(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString) {
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.TEMPERATURE);
//		Double temperature = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getTemperature();
//		// dealWithCurrentValue(temperature,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (temperature >= idealMin && temperature <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (temperature <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(temperature, idealValue, idealMin));
//
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(temperature, idealValue, idealMax));
//
//			}
//		} else if (temperature >= acceptMin && temperature <= acceptMax) {
//			if (temperature <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(temperature, idealMin, acceptMin));
//
//			} else if (temperature >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(temperature, idealMax, acceptMax));
//
//			}
//		} else if (temperature < acceptMin || temperature > acceptMax) {
//			if (temperature < acceptMin)
//				scoreAndExplain.setExplain(overLeastString);
//			else
//				scoreAndExplain.setExplain(overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(temperature, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	
//	private void computeHumidity(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString) {
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.HUMIDITY);
//		Double humidity = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getHumidity();
//		// dealWithCurrentValue(humidity,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (humidity >= idealMin && humidity <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (humidity <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(humidity, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(humidity, idealValue,
//								idealMax));
//			}
//		} else if (humidity >= acceptMin && humidity <= acceptMax) {
//			if (humidity <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(humidity, idealMin,
//								acceptMin));
//			} else if (humidity >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(humidity, idealMax,
//								acceptMax));
//			}
//		} else if (humidity < acceptMin || humidity > acceptMax) {
//			if (humidity < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(humidity, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	
//	private void computeLightning(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.LIGHTING);
//		Double lighting = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getLighting();
//		// dealWithCurrentValue(lighting,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (lighting >= idealMin && lighting <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (lighting <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(lighting, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(lighting, idealValue,
//								idealMax));
//			}
//		} else if (lighting >= acceptMin && lighting <= acceptMax) {
//			if (lighting <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(lighting, idealMin,
//								acceptMin));
//			} else if (lighting >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(lighting, idealMax,
//								acceptMax));
//			}
//		} else if (lighting < acceptMin || lighting > acceptMax) {
//			if (lighting < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(lighting, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	
//	private void computeUv(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.UV);
//		Double uv = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getUvIntensity();
//		// dealWithCurrentValue(uv,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (uv >= idealMin && uv <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (uv <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(uv, idealValue, idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(uv, idealValue, idealMax));
//			}
//		} else if (uv >= acceptMin && uv <= acceptMax) {
//			if (uv <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(uv, idealMin, acceptMin));
//			} else if (uv >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(uv, idealMax, acceptMax));
//			}
//		} else if (uv < acceptMin || uv > acceptMax) {
//			if (uv < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(uv, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//tvoc
//	private void computeTvoc(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.TVOC);
//		Double tvoc = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getVoc();
//		// dealWithCurrentValue(tvoc,idealValue,idealMin,idealMax,acceptMin,acceptMax,fluctuatuionCoefficient,ipcanContext);
//		if (tvoc >= idealMin && tvoc <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (tvoc <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(tvoc, idealValue, idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(tvoc, idealValue, idealMax));
//			}
//		} else if (tvoc >= acceptMin && tvoc <= acceptMax) {
//			if (tvoc <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(tvoc, idealMin, acceptMin));
//			} else if (tvoc >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(tvoc, idealMax, acceptMax));
//			}
//		} else if (tvoc < acceptMin || tvoc > acceptMax) {
//			if (tvoc < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(tvoc, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm2_5
//	private void computePm2_5(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM2_5);
//		Double pm2_5 = 0.0;
//
//		pm2_5 = ipcanContext.getRecievedMuseumDomain().getDomainBody()
//				.getPmPojo().getValue();
//		if (pm2_5 >= idealMin && pm2_5 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm2_5 <= idealValue) {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(pm2_5, idealValue,
//										idealMin));
//			} else {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(pm2_5, idealValue,
//										idealMax));
//			}
//		} else if (pm2_5 >= acceptMin && pm2_5 <= acceptMax) {
//			if (pm2_5 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(pm2_5, idealMin,
//										acceptMin));
//			} else if (pm2_5 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(pm2_5, idealMax,
//										acceptMax));
//			}
//		} else if (pm2_5 < acceptMin || pm2_5 > acceptMax) {
//			if (pm2_5 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm2_5, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm10
//	private void computePm10(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM10);
//		Double pm10 = 0.0;
//
//		pm10 = ipcanContext.getRecievedMuseumDomain().getDomainBody()
//				.getPmPojo().getValue();
//		if (pm10 >= idealMin && pm10 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm10 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm10, idealValue, idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm10, idealValue, idealMax));
//			}
//		} else if (pm10 >= acceptMin && pm10 <= acceptMax) {
//			if (pm10 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm10, idealMin, acceptMin));
//			} else if (pm10 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm10, idealMax, acceptMax));
//			}
//		} else if (pm10 < acceptMin || pm10 > acceptMax) {
//			if (pm10 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm10, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm1_0
//	private void computePm1_0(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM1_0);
//		Double pm1_0 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getPmPojo().getValue();
//		if (pm1_0 >= idealMin && pm1_0 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm1_0 <= idealValue) {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(pm1_0, idealValue,
//										idealMin));
//			} else {
//				scoreAndExplain.setScore(
//								getIdealRangeScore(pm1_0, idealValue,
//										idealMax));
//			}
//		} else if (pm1_0 >= acceptMin && pm1_0 <= acceptMax) {
//			if (pm1_0 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(pm1_0, idealMin,
//										acceptMin));
//			} else if (pm1_0 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//								getAcceptRangeScore(pm1_0, idealMax,
//										acceptMax));
//			}
//		} else if (pm1_0 < acceptMin || pm1_0 > acceptMax) {
//			if (pm1_0 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm1_0, idealValue, acceptMin,
//							acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm_01_03
//	private void computePm_01_03(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM_01_03);
//		Double pm_01_03 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getPmPojo().getValue();
//		if (pm_01_03 >= idealMin && pm_01_03 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm_01_03 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_03, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_03, idealValue,
//								idealMax));
//			}
//		} else if (pm_01_03 >= acceptMin && pm_01_03 <= acceptMax) {
//			if (pm_01_03 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_03, idealMin,
//								acceptMin));
//			} else if (pm_01_03 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_03, idealMax,
//								acceptMax));
//			}
//		} else if (pm_01_03 < acceptMin || pm_01_03 > acceptMax) {
//			if (pm_01_03 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm_01_03, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm_01_05
//	private void computePm_01_05(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM_01_05);
//		Double pm_01_05 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getPmPojo().getValue();
//		if (pm_01_05 >= idealMin && pm_01_05 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm_01_05 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_05, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_05, idealValue,
//								idealMax));
//			}
//		} else if (pm_01_05 >= acceptMin && pm_01_05 <= acceptMax) {
//			if (pm_01_05 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_05, idealMin,
//								acceptMin));
//			} else if (pm_01_05 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_05, idealMax,
//								acceptMax));
//			}
//		} else if (pm_01_05 < acceptMin || pm_01_05 > acceptMax) {
//			if (pm_01_05 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm_01_05, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm_01_10
//	private void computePm_01_10(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM_01_10);
//		Double pm_01_10 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getPmPojo().getValue();
//		if (pm_01_10 >= idealMin && pm_01_10 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm_01_10 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_10, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_10, idealValue,
//								idealMax));
//			}
//		} else if (pm_01_10 >= acceptMin && pm_01_10 <= acceptMax) {
//			if (pm_01_10 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_10, idealMin,
//								acceptMin));
//			} else if (pm_01_10 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_10, idealMax,
//								acceptMax));
//			}
//		} else if (pm_01_10 < acceptMin || pm_01_10 > acceptMax) {
//			if (pm_01_10 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm_01_10, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//pm_01_50
//	private void computePm_01_50(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM_01_50);
//		Double pm_01_50 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getPmPojo().getValue();
//		if (pm_01_50 >= idealMin && pm_01_50 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm_01_50 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_50, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_50, idealValue,
//								idealMax));
//			}
//		} else if (pm_01_50 >= acceptMin && pm_01_50 <= acceptMax) {
//			if (pm_01_50 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_50, idealMin,
//								acceptMin));
//			} else if (pm_01_50 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_50, idealMax,
//								acceptMax));
//			}
//		} else if (pm_01_50 < acceptMin || pm_01_50 > acceptMax) {
//			if (pm_01_50 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm_01_50, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	//Pm_01_100
//	private void computePm_01_100(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.PM_01_100);
//		Double pm_01_100 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getPmPojo().getValue();
//		if (pm_01_100 >= idealMin && pm_01_100 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (pm_01_100 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_100, idealValue,
//								idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(pm_01_100, idealValue,
//								idealMax));
//			}
//		} else if (pm_01_100 >= acceptMin && pm_01_100 <= acceptMax) {
//			if (pm_01_100 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_100, idealMin,
//								acceptMin));
//			} else if (pm_01_100 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(pm_01_100, idealMax,
//								acceptMax));
//			}
//		} else if (pm_01_100 < acceptMin || pm_01_100 > acceptMax) {
//			if (pm_01_100 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(pm_01_100, idealValue,
//							acceptMin, acceptMax));
//		}
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	
//	//甲醛
//	private void computeCh4(IpcanContext ipcanContext,RealTimeScoreAndExplain realTimeScore, Double idealValue,
//			Double idealMin, Double idealMax, Double acceptMin,
//			Double acceptMax, String idealString, String lessString,
//			String moreString, String overMostString, String overLeastString){
//		SocreAndExplainPojo scoreAndExplain = new SocreAndExplainPojo();
//		scoreAndExplain.setMonitorObjectType(MonitorObjectType.CH4);
//		Double ch4 = ipcanContext.getRecievedMuseumDomain()
//				.getDomainBody().getFormalclehyde();
//		if (ch4 >= idealMin && ch4 <= idealMax) {
//			scoreAndExplain.setExplain(idealString);
//			if (ch4 <= idealValue) {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(ch4, idealValue, idealMin));
//			} else {
//				scoreAndExplain.setScore(
//						getIdealRangeScore(ch4, idealValue, idealMax));
//			}
//		} else if (ch4 >= acceptMin && ch4 <= acceptMax) {
//			if (ch4 <= idealMin) {
//				scoreAndExplain.setExplain(lessString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(ch4, idealMin, acceptMin));
//			} else if (ch4 >= idealMax) {
//				scoreAndExplain.setExplain(moreString);
//				scoreAndExplain.setScore(
//						getAcceptRangeScore(ch4, idealMax, acceptMax));
//			}
//		} else if (ch4 < acceptMin || ch4 > acceptMax) {
//			if (ch4 < acceptMin)
//				scoreAndExplain.setExplain(
//						overLeastString);
//			else
//				scoreAndExplain.setExplain(
//						overMostString);
//			scoreAndExplain.setScore(
//					getWarningRangScore(ch4, idealValue, acceptMin,
//							acceptMax));
//		}	
//		realTimeScore.getScoreAndExplain().add(scoreAndExplain);
//		ipcanContext.setRealTimeScore(realTimeScore);
//	}
//	
//	/**
//	 * 处理实时数据，进行对象评分
//	 * 
//	 * @param ipcanContext
//	 * @return ipcanContext
//	 */
//	public void doWithRealTimeData(IpcanContext ipcanContext) {
//		// 获得数据上报的区域
//		RepoArea area = ipcanContext.getRunTimeInfo().getRepoArea();
//		// 根据区域在业务评分表中查询相应的对象的标准
//		// 找到该区域对应的标准，拿到该标准
//		if (area != null && area.getId() != null)
//			area = areaDao.findOne(area.getId());
//		if (area == null)
//			return;
//		CustomizedStandard customizedStandard = customizedStandardDao
//				.findOneByArea(area);
//		Standard standard = null;
//		StandardDomain standardDomain = new StandardDomain();
//		ExplainDomain explainDomain = new ExplainDomain();
//		RealTimeScoreAndExplain realTimeScore = new RealTimeScoreAndExplain();
//		
//		// TODO:定制标准中，是参考重要，还是定制的重要
//		if (customizedStandard != null) {
//			if (customizedStandard.getStandard() != null) {
//				standard = standardDao.findOne(customizedStandard.getStandard()
//						.getId());
//				standardDomain = JSONUtil.deserialize(standard.getValue(),
//						StandardDomain.class);
//				explainDomain = JSONUtil.deserialize(standard.getExplain(),
//						ExplainDomain.class);
//			} else if (customizedStandard.getValue() != null) {
//				standardDomain = JSONUtil.deserialize(
//						customizedStandard.getValue(), StandardDomain.class);
//			} else {
//				return;
//			}
//		} else
//			return;
//
//		// 反编译json串
//		// standardDomain = JSONUtil.deserialize(standard.getValue(),
//		// StandardDomain.class);
//		// 获得标准具体的值
//		List<AtomicStandardDomain> standards = standardDomain.getStandards();
//		List<AtomicExplainDomain> explains = explainDomain.getExplains();
//
//		for (AtomicStandardDomain atomicStandardDomain : standards) {
//
//			Double idealValue = atomicStandardDomain.getIdealValue();
//			Double idealMin = atomicStandardDomain.getIdealMin();
//			Double idealMax = atomicStandardDomain.getIdealMax();
//			Double acceptMin = atomicStandardDomain.getAcceptMin();
//			Double acceptMax = atomicStandardDomain.getAcceptMax();
//			Double fluctuatuionCoefficient = atomicStandardDomain
//					.getFluctuatuionCoefficient();
//			String idealString = null;
//			String lessString = null;
//			String overLeastString = null;
//			String moreString = null;
//			String overMostString = null;
//			for (AtomicExplainDomain atomicExplainDomain : explains) {
//				if (atomicExplainDomain.getMonitorObjectType() == atomicStandardDomain
//						.getMonitorObjectType()) {
//					idealString = atomicExplainDomain.getIdealString();
//					lessString = atomicExplainDomain.getLessString();
//					overLeastString = atomicExplainDomain.getOverLeastString();
//					moreString = atomicExplainDomain.getMoreString();
//					overMostString = atomicExplainDomain.getOverMostString();
//				}
//			}
//
//			if (atomicStandardDomain.getMonitorObjectType().getValue()
//					.equals(MonitorObjectType.SO2.getValue())
//					&& ipcanContext.getRecievedMuseumDomain().getDomainBody()
//							.getSulfurDioxide() != null) {
//				this.computeSO2(ipcanContext,realTimeScore, idealValue, idealMin, idealMax,
//						acceptMin, acceptMax, idealString, lessString,
//						moreString, overMostString, overLeastString);
//
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getCarbonDioxide() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.CO2.getValue())) {
//				this.computeCO2(ipcanContext,realTimeScore, idealValue, idealMin, idealMax,
//						acceptMin, acceptMax, idealString, lessString,
//						moreString, overMostString, overLeastString);
//
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getTemperature() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.TEMPERATURE.getValue())) {
//				this.computeTemperature(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getHumidity() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.HUMIDITY.getValue())) {
//				this.computeHumidity(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//				
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getLighting() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.LIGHTING.getValue())) {
//				this.computeLightning(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);				
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getUvIntensity() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.UV.getValue())) {
//				this.computeUv(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//				
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getVoc() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.TVOC.getValue())) {
//				this.computeTvoc(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);				
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getPmPojo().getPmType() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM2_5.getValue())) {
//				this.computePm2_5(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//				
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getPmPojo().getPmType() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM10.getValue())) {
//				this.computePm10(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM1_0.getValue())) {
//				this.computePm1_0(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM_01_03.getValue())) {
//				this.computePm_01_03(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//				
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM_01_05.getValue())) {
//				this.computePm_01_05(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM_01_10.getValue())) {
//				this.computePm_01_10(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM_01_50.getValue())) {
//				this.computePm_01_50(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.PM_01_100.getValue())) {
//				this.computePm_01_100(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);
//			} else if (ipcanContext.getRecievedMuseumDomain().getDomainBody()
//					.getFormalclehyde() != null
//					&& atomicStandardDomain.getMonitorObjectType().getValue()
//							.equals(MonitorObjectType.CH4.getValue())) {
//				this.computeCh4(ipcanContext,realTimeScore, idealValue, idealMin, idealMax, acceptMin, acceptMax, idealString, lessString, moreString, overMostString, overLeastString);	
//			}
//		}
//		return;
//	}
//
//}
