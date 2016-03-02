/**
 2014年11月23日
 14cells
 
 */
package com.qingruan.museum.domain.enums;

/**
 * 定义消息方向{Netty---Engine,Engine---Netty,Engine---Admin,Admin---Engine}
 * 
 * @author tommy
 * 
 */
public enum MessageAction {
	NETTY_TO_ENGINE, ENGINE_TO_NETTY, ENGINE_TO_ADMIN, ADMIN_TO_ENGINE;

}
