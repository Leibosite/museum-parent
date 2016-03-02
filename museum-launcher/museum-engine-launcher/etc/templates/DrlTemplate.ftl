<#setting number_format="computer">
<#list imports as import>
import ${import};
</#list>
<#list interfaceModels as interfaceModel>
global ${interfaceModel.packageInfo}.${interfaceModel.className} ${interfaceModel.callingExpress};
</#list>
rule "${ruleName}"
salience ${salience}
when 
      <#list whenBean.normalPatterns as normalPattern>
      ${normalPattern.patternName}:${normalPattern.ipatternName}(<#list normalPattern.memberConstraints as memberConstraint>${memberConstraint.memberFetchExp}${memberConstraint.memberConstraintOperator}<#if memberConstraint_has_next>,</#if></#list>);
      </#list>
      delayTaskAgency:DelayTaskAgency();
      eval(<#list whenBean.serviceConstraints as constraint>${constraint.callingExpress}(<#list constraint.serviceConstraintMethodParams as serviceConstraintMethodParam>${serviceConstraintMethodParam}<#if serviceConstraintMethodParam_has_next>,</#if></#list>)${constraint.memberConstraintOperator}<#if constraint_has_next>&&</#if></#list>);
then  
      delayTaskAgency.addEffectPolicy(${policyId}L);
      DelayTask delayTask=null;
      <#list delayTaskBeans as delayTaskBean>
       delayTask=new DelayTask(${delayTaskBean.paramSize});
	   delayTask.setBusinessServiceName("${delayTaskBean.businessServiceName}");
	  <#list delayTaskBean.params as param>
	   delayTask.addParam(${param});
	  </#list>
	  delayTask.setSalience(${delayTaskBean.salience});
	  delayTask.setPolicyId(${delayTaskBean.policyId});
	  delayTaskAgency.addDelayTask(delayTask);
	  </#list>
end