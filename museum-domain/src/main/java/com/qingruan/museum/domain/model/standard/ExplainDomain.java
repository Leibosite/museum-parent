package com.qingruan.museum.domain.model.standard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author leibosite
 *
 */
@Setter
@Getter
public class ExplainDomain {
	private static final long serialVersionUID = -104812950171489322L;
	private List<AtomicExplainDomain> explains = new ArrayList<AtomicExplainDomain>();
}
