/**
 2015年2月14日
 14cells
 
 */
package com.qingruan.museum.session;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.domain.models.pojo.SocreAndExplainPojo;

/**
 * @author tommy
 *
 */
@Getter
@Setter
public class RealTimeScoreAndExplain {
	
	List<SocreAndExplainPojo> scoreAndExplain = new ArrayList<SocreAndExplainPojo>();
	
	
}
