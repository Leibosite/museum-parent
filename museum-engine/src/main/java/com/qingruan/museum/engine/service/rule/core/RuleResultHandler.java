//package com.qingruan.museum.engine.service.rule.core;
//
//import java.util.List;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.baoyun.pcrf.domain.enums.AccessType;
//import com.baoyun.pcrf.domain.signaling.Bearer;
//import com.baoyun.pcrf.domain.signaling.BearerControlMode;
//import com.baoyun.pcrf.domain.signaling.CCRequestType;
//import com.baoyun.pcrf.domain.signaling.CommandHead;
//import com.baoyun.pcrf.domain.signaling.CommandTypeIndicator;
//import com.baoyun.pcrf.domain.signaling.ExperimentalResult;
//import com.baoyun.pcrf.domain.signaling.ExperimentalResultCode;
//import com.baoyun.pcrf.domain.signaling.InterfaceIndicator;
//import com.baoyun.pcrf.domain.signaling.NetworkRequestSupport;
//import com.baoyun.pcrf.domain.signaling.ResultCode;
//import com.baoyun.pcrf.domain.signaling.SignalingDomain;
//import com.baoyun.pcrf.domain.signaling.SignalingResult;
//import com.baoyun.pcrf.domain.signaling.SupportedFeatures;
//import com.baoyun.pcrf.domain.validcheck.DomainValidCheck;
//import com.baoyun.pcrf.session.DiameterParamModel;
//import com.baoyun.pcrf.session.GxaDiameterParamModel;
//import com.baoyun.pcrf.session.IpcanContext;
//import com.baoyun.pcrf.session.repository.GxaDiameterParamDao;
//
//@Slf4j
//@Component
//public class RuleResultHandler {
//
//	private static final long VENDOR_ID = 10415;
//	// private static final int FEATURE_LIST_ID = 1;
//	// private static final int FEATURE_LIST = 3;
//
//	@Autowired
//	private GxaDiameterParamDao gxaDiameterParamDao;
//
//	/**
//	 * default method for result code
//	 * 
//	 * @param cc
//	 */
//	public void organizeCcrRequestResult(IpcanContext ipcanContext) {
//
//		organizeCcrRequestResult(ipcanContext, ResultCode.DIAMETER_SUCCESS);
//
//	}
//
//	public void organizeCcrRequestResult(IpcanContext ipcanContext,
//			ResultCode resultCode) {
//		log.debug("organizeCcrRequestResult-------------start");
//		SignalingDomain signalingDomainReceived = ipcanContext
//				.getSignalingDomainReceived();
//
//		SignalingDomain signalingDomainToSend = ipcanContext
//				.getSignalingDomainToSend();
//
//		signalingDomainToSend.interfaceIndicator = signalingDomainReceived.interfaceIndicator;
//		signalingDomainToSend.commonProtocolInfo = signalingDomainReceived.commonProtocolInfo;
//
//		CommandHead commandHeadReceived = signalingDomainReceived.commandHead;
//		CommandHead commandHead = DomainValidCheck
//				.CommandHeadPrepare(signalingDomainToSend);
//		commandHead.cCRequestNumber = commandHeadReceived.cCRequestNumber;
//		commandHead.cCRequestType = commandHeadReceived.cCRequestType;
//		// 原先代码的回复错误，应该是ccr的dest-host对应cca的origin-host
//		// commandHead.originHost = commandHeadReceived.originHost;
//		// commandHead.originRealm = commandHeadReceived.originRealm;
//		commandHead.originHost = commandHeadReceived.destinationHost;
//		commandHead.originRealm = commandHeadReceived.destinationRealm;
//		commandHead.sessionId = commandHeadReceived.sessionId;
//
//		signalingDomainToSend.proxyInfos = signalingDomainReceived.proxyInfos;
//
//		assembleResultCode(signalingDomainToSend, resultCode);
//
//		if (commandHead.cCRequestType == CCRequestType.INITIAL_REQUEST
//				&& resultCode == ResultCode.DIAMETER_SUCCESS) {
//			handleBearerControlMode(ipcanContext);
//		}
//
//		log.debug("organizeCcrRequestResult-------------end");
//	}
//
//	public void assembleResultCode(SignalingDomain signalingDomain,
//			ResultCode resultCode) {
//		SignalingResult signalingResult = DomainValidCheck
//				.SignalingResultPrepare(signalingDomain);
//		signalingResult.resultCode = resultCode;
//	}
//
//	public void assembleExperimentalResult(SignalingDomain signalingDomain,
//			ExperimentalResultCode experimentalResultCode) {
//		ExperimentalResult experimentalResult = DomainValidCheck
//				.ExperimentalResultPrepare(signalingDomain);
//
//		experimentalResult.experimentalResultCode = experimentalResultCode;
//
//		experimentalResult.vendorId = VENDOR_ID;
//	}
//
//	public void organizeGxaRarProvisionResult(IpcanContext ipcanContext) {
//		log.debug("organizeGxaRarProvisionResult-------------start");
//
//		GxaDiameterParamModel dModel = gxaDiameterParamDao
//				.findByUserId(ipcanContext.getBusinessInfo().getUserId());
//		SignalingDomain signalingDomainToSend = ipcanContext
//				.getSignalingDomainToSend();
//
//		signalingDomainToSend.interfaceIndicator = dModel
//				.getInterfaceIndicator();
//
//		CommandHead commandHead = DomainValidCheck
//				.CommandHeadPrepare(signalingDomainToSend);
//
//		commandHead.destinationHost = dModel.getGxaDestinationHost();
//
//		commandHead.destinationRealm = dModel.getGxaDestinationRealm();
//
//		commandHead.sessionId = dModel.getGxaSessionId();
//
//		signalingDomainToSend.commonProtocolInfo = dModel
//				.getGxaCommonProtocolInfo();
//
//		// if (dModel.getGxaDiameterProxyInfos() != null) {
//		// signalingDomainToSend.proxyInfos = BusinessInfoService
//		// .toProxyInfos(dModel.getGxaDiameterProxyInfos());
//		// }
//
//		log.debug("organizeGxaRarProvisionResult-------------end");
//	}
//
//	private void organizeBearerControlMode(IpcanContext ipcanContext) {
//		NetworkRequestSupport networkRequestSupport = DomainValidCheck
//				.NetworkRequestSupportExist(ipcanContext
//						.getSignalingDomainReceived());
//		if (networkRequestSupport == null) {
//			log.info("networkRequestSupport is null.");
//
//			return;
//		}
//
//		BearerControlMode bearerControlMode;
//
//		if (networkRequestSupport == NetworkRequestSupport.NETWORK_REQUEST_SUPPORTED) {
//			bearerControlMode = BearerControlMode.UE_NW;
//		} else {
//			bearerControlMode = BearerControlMode.UE_ONLY;
//		}
//
//		Bearer bearer = DomainValidCheck.BearerPrepare(ipcanContext
//				.getSignalingDomainToSend());
//
//		bearer.bearerControlMode = bearerControlMode;
//	}
//
//	public void handleBearerControlMode(IpcanContext ipcanContext) {
//		log.debug("handleBearerControlMode-------------start");
//
//		AccessType accessType = ipcanContext.getBusinessInfo().getAccessType();
//		if (accessType == null) {
//			log.info("accessType is null.");
//
//			return;
//		}
//
//		SignalingDomain signalingDomainReceived = ipcanContext
//				.getSignalingDomainReceived();
//
//		InterfaceIndicator interfaceIndicator = signalingDomainReceived.interfaceIndicator;
//
//		if (interfaceIndicator == InterfaceIndicator.GX) {
//			if (accessType == AccessType.EHRPD) {
//				log.info(
//						"interfaceIndicator is {}, accessType is {}. INVALID.",
//						interfaceIndicator, accessType);
//
//				return;
//			}
//		} else if (interfaceIndicator == InterfaceIndicator.GXA) {
//			if (accessType != AccessType.EHRPD) {
//				log.info(
//						"interfaceIndicator is {}, accessType is not {}. INVALID.",
//						interfaceIndicator, AccessType.EHRPD);
//
//				return;
//			}
//		} else {
//			log.info("interfaceIndicator is {}, do not know how to handle.",
//					interfaceIndicator);
//
//			return;
//		}
//
//		organizeBearerControlMode(ipcanContext);
//
//		log.debug("handleBearerControlMode-------------end");
//	}
//
//	public void organizeRarProvisionResult(IpcanContext ipcanContext,
//			DiameterParamModel dModel) {
//		log.debug("organizeRarProvisionResult-------------start");
//
//		SignalingDomain signalingDomainToSend = ipcanContext
//				.getSignalingDomainToSend();
//
//		signalingDomainToSend.interfaceIndicator = dModel
//				.getInterfaceIndicator();
//
//		CommandHead commandHead = DomainValidCheck
//				.CommandHeadPrepare(signalingDomainToSend);
//
//		// TODO
//		commandHead.destinationHost = dModel.getDestinationHost();
//
//		commandHead.destinationRealm = dModel.getDestinationRealm();
//
//		commandHead.originHost = dModel.getOriginHost();
//		commandHead.originRealm = dModel.getOriginRealm();
//
//		// commandHead.destinationHost = "pcrf.baoyunnetworks.com";
//		//
//		// commandHead.destinationRealm = "baoyunnetworks.com";
//
//		commandHead.sessionId = dModel.getSessionId();
//
//		signalingDomainToSend.commonProtocolInfo = dModel
//				.getCommonProtocolInfo();
//
//		// if (dModel.getDiameterProxyInfos() != null) {
//		// signalingDomainToSend.proxyInfos = BusinessInfoService
//		// .toProxyInfos(dModel.getDiameterProxyInfos());
//		// }
//
//		log.debug("organizeRarProvisionResult-------------end");
//	}
//
//	public void organizeSupportFeature(IpcanContext ipcanContext) {
//		if (ipcanContext != null) {
//			SignalingDomain signalingDomainToSend = ipcanContext
//					.getSignalingDomainToSend();
//			List<SupportedFeatures> supportedFeaturess = DomainValidCheck
//					.SupportedFeaturessExist(ipcanContext
//							.getSignalingDomainReceived());
//
//			if (signalingDomainToSend != null && supportedFeaturess != null) {
//				log.info("organizeSupportFeature-------Start add support feature");
//
//				CommandHead commandHead = DomainValidCheck
//						.CommandHeadPrepare(signalingDomainToSend);
//
//				commandHead.supportedFeaturess = supportedFeaturess;
//
//				log.info("organizeSupportFeature-------End add support feature");
//
//			}
//
//		}
//
//	}
//
//	public void organizeSlaResult(IpcanContext ipcanContext) {
//
//	}
//
//	public void setToSendRar(IpcanContext ipcanContext) {
//		SignalingDomain signalingDomainToSend = ipcanContext
//				.getSignalingDomainToSend();
//
//		if (signalingDomainToSend.commandTypeIndicator == CommandTypeIndicator.RAR) {
//			ipcanContext.getRuntimeInfo().setToSendRar(true);
//		}
//	}
//
//	public boolean checkNotToSendRar(IpcanContext ipcanContext) {
//		SignalingDomain signalingDomainToSend = ipcanContext
//				.getSignalingDomainToSend();
//
//		/*
//		 * isToSendRar 实际标识：bs方法什么都没做 FIXME
//		 */
//		if (signalingDomainToSend.commandTypeIndicator == CommandTypeIndicator.RAR
//				&& ipcanContext.getRuntimeInfo().isToSendRar() == false) {
//			return true;
//		}
//
//		return false;
//	}
//}
