
package com.qingruan.museum.pma.drlbuilder.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:NormalPattern <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2014-5-29 下午4:33:59 <br/>
 * @author   tommy
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Setter
@Getter
public class NormalPatternBean {
    private String patternName;
    private String ipatternName;
    private List<ConstraintBean> memberConstraints;
}

