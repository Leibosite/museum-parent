/**
 2015年1月15日
 14cells
 
 */
package com.qingruan.museum.engine.service.rule.core;

import com.qingruan.museum.engine.service.rule.DelayTaskAgency;

/**
 * @author tommy
 * 
 */
public interface DelayTaskSuperadder {
	public void superadd(DelayTaskAgency delayTaskAgency, Object param);

}
