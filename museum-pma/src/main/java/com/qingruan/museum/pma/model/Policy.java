package com.qingruan.museum.pma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.dao.entity.IdEntityTimeStamp;
import com.qingruan.museum.dao.entity.PolicyGroup;

@Getter
@Setter
public class Policy extends IdEntityTimeStamp implements Serializable {
	private static final long serialVersionUID = -504857129571489352L;

	public static final String STATUS_MARK = "status";

	public static final String POLICY_NAME_MARK = "policyName";

	private String policyName;
	private Integer salience;// 优先级。值大，优先级高
	private List<Constraint> pattern;
	private List<Constraint> constraints = new ArrayList<Constraint>();
	private List<Execution> executions = new ArrayList<Execution>();
	private Boolean status;// for enable or disable
	private PolicyGroup policyGroup;
	private String desp;

}
