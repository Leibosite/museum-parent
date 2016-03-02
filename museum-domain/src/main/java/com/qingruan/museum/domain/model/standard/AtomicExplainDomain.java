package com.qingruan.museum.domain.model.standard;
/**
 * @desp 原子 解释词典
 * @author leibosite
 */
import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.enums.MonitorDataType;

@Getter
@Setter
public class AtomicExplainDomain {
	private static final long serialVersionUID = -804812950171089352L;
	private MonitorDataType monitorObjectType;
	private String idealString;
	private String lessString;
	private String overLeastString;
	private String moreString;
	private String overMostString;
}
