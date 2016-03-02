package com.qingruan.museum.pma.drlbuilder.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:NormalPatternBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-5-29 下午2:53:54 <br/>
 * 
 * @author tommy
 * @version
 * @since JDK 1.6
 * @see
 */
@Setter
@Getter
public class WhenBean  {
    // normalPatterns
    private List<NormalPatternBean> normalPatterns;
    // eval
    private List<ConstraintBean> serviceConstraints;
}
