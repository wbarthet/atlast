<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="pops" type="java.util.List<org.atlast.beans.Pop>" -->

<h1>Population</h1>

<div>
  <ul>
  <#list pops as pop>
    <li>
      <h2><a href="<@hst.link hippobean=pop/>">${pop.name} (${pop.popClass?cap_first} Class)</a></h2>
      <p>Cash: ${pop.cash}</p>
      <p>Food: ${pop.food}</p>
      <p>Goods: ${pop.goods}</p>
      <p>Luxuries: ${pop.luxuries}</p>
        <#if pop.land.development>
          <p>Job: ${pop.land.name} (${pop.land.developmentDescriptor.name})</p>
        </#if>
    </li>

  </#list>
  </ul>
</div>