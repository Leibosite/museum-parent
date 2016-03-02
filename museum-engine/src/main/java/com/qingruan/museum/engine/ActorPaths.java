package com.qingruan.museum.engine;
/**
 * 
 * @author tommy
 *
 */
public interface ActorPaths {
	final static String ADMINSUB_ROUTE = "adminsub.route";
	final static String ADMINSUB_RULE = "adminsub.rule";
	final static String ADMINSUB_LICENSE_ABNORMITY = "adminsub.license-abnormity";
	final static String ADMIN_TASK_TRIGGER = "admin.task-trigger";
	final static String ADMIN_BACK_TO_STACK = "admin.back-to-stack";
	final static String ADMIN_ROUTE = "admin.route";
	final static String ADMIN_SESSION_REMOVE = "admin.session-remove";
	final static String ADMIN_RAR_TRIGGER = "admin.rar-trigger";
	final static String ADMIN_SESSION_HEART_BEAT = "admin.session-heart-beat";
	final static String GX_UPDATE = "gx.update";
	final static String GX_TERMINATE = "gx.terminate";
	final static String GX_RAR_SESSION_HEART_BEAT = "gx.rar-session-heart-beat";
	final static String GX_BACK_TO_STACK = "gx.back-to-stack";

	// Rx Start
	final static String RX_BACK_TO_STACK = "rx.back-to-stack";
	final static String RX_ROUTE = "rx.route";

	final static String RX_ESTABLISH = "rx.establish";
	final static String RX_UPDATE = "rx.update";
//	final static String RX_RAA_WORKFLOW = "rx.aaa";//TODO

	final static String RX_TERMINATE = "rx.terminate";
	final static String RX_STA_WORKFLOW = "rx.sta";

	final static String RX_ASR = "rx.asr";

	final static String RX_RAR_DEACTIVE_FLOW = "rx.rar-deactive-flow";
	final static String RX_REPORT_USAGE_MONITORING_WORKFLOW = "rx.report-usage-monitoring";

	// Rx End

	final static String GX_RAR_SYNCHRONIZER = "gx.rar-synchronizer";
	final static String GX_ROUTE = "gx.route";
	final static String GX_TASK_RAR = "gx.task-rar";
	final static String SY_RAR_STOP = "sy.rar-stop";
	final static String SY_BACK_TO_STACK = "sy.back-to-stack";
	final static String SY_ROUTE = "sy.route";
	final static String SY_RAR_ESTABLISH = "sy.rar-establish";
	final static String SY_REPORT = "sy.report";
	final static String SY_SLA = "sy.sla";
	final static String GX_ESTABLISH = "gx.establish";
	final static String GX_RAA = "gx.raa";
	final static String GX_RAR_OCS_CONTROL = "gx.rar-ocs-control";
	final static String GX_RAR_KICK_OFF = "gx.rar-kick-off";
	final static String GXA_UPDATE = "gxa.update";
	final static String GXA_TERMINATE = "gxa.terminate";
	final static String GXA_BACK_TO_STACK = "gxa.back-to-stack";
	final static String GXA_RAR_SYNCHRONIZER = "gxa.rar-synchronizer";
	final static String GXA_ROUTE = "gxa.route";
	final static String GXA_TASK_RAR = "gxa.task-rar";
	final static String GXA_ESTABLISH = "gxa.establish";

	// cdr message
	final static String CDR_MSG_SEND = "cdrmsg.send";

	// sms message
	final static String SMS_SEND = "smssend.send";

}
