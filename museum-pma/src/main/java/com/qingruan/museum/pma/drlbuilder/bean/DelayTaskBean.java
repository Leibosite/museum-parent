/**
 * Project Name:pcrf-pma
 * File Name:DelayTaskBean.java
 * Package Name:com.baoyun.pcrf.pma.drlbuilder.bean
 * Date:2014-5-28下午6:04:00
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.qingruan.museum.pma.drlbuilder.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:DelayTaskBean <br/>
 * Reason:   DRL文件中then部分的DelayTask映射对象 <br/>
 * Date:     2014-5-28 下午6:04:00 <br/>
 * @author   tommy
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Getter
@Setter
public class DelayTaskBean  {
private int paramSize;
private String businessServiceName;
private List<String> params;
private int salience;
private String policyId;
}

