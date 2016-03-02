/**
 2015年2月14日
 14cells
 
 */
package com.qingruan.museum.domain.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 14cells
 * 
 */
@Setter
@Getter
public class StandardDomain implements Serializable {
	private static final long serialVersionUID = -104812950171489352L;
	
	private List<AtomicStandardDomain> standards = new ArrayList<AtomicStandardDomain>();
	
//	private HashMap<MonitorObjectType, AtomicStandardDomain> maps = new HashMap<MonitorObjectType, AtomicStandardDomain>();

}
