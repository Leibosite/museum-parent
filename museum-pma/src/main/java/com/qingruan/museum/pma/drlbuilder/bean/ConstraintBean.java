package com.qingruan.museum.pma.drlbuilder.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ConstraintBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-5-29 下午3:13:53 <br/>
 * 
 * @author tommy
 * @version
 * @since JDK 1.6
 * @see
 */
@Setter
@Getter
public class ConstraintBean  {
    private String memberFetchExp;
    private String memberConstraintOperator;
    private String CallingExpress;
    private List<String> serviceConstraintMethodParams;
}
