package com.qingruan.museum.pma.drlbuilder.bean;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import com.qingruan.museum.pma.meta.InterfaceModel;

import freemarker.ext.beans.BeansWrapper;

/**
 * ClassName:TemplatesBean <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: drl模版bean <br/>
 * Date: 2014-5-28 下午4:06:28 <br/>
 * 
 * @author tommy
 * @version
 * @since JDK 1.7
 * @see
 */
@Getter
@Setter
public class DRLTemplatesBean extends BeansWrapper {
    // imports
    private Set<String> imports;
    // Globles
    private Set<InterfaceModel>interfaceModels;
   
    // 开始部分对象
    private String ruleName;
    private int salience;
    private Long policyId;
    // when部分对象
    private WhenBean whenBean;
    // then部分 对象
    private List<DelayTaskBean> delayTaskBeans;
}
