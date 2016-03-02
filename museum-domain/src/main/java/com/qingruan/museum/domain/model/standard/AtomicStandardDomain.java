/**
 2015年2月14日
 14cells
 
 */
package com.qingruan.museum.domain.model.standard;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

/**
 * 标准定制的Domain
 * 
 * @author tommy
 * 
 */
@Getter
@Setter
public class AtomicStandardDomain implements Serializable {
	private static final long serialVersionUID = -504812950171489352L;
	private Long id;
	private String name;
	private String desp;
	private Integer status;
	// 监测对象类型
	private MonitorDataType monitorObjectType;
	// 理想值
	private Double idealValue;
	// 理想最小值
	private Double idealMin;
	// 理想最大值
	private Double idealMax;
	// 可接受最小值
	private Double acceptMin;
	// 可接受最大值
	private Double acceptMax;
	// 波动系数
	private Double fluctuatuionCoefficient;
}
