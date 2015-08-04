<#include "../../include/imports.ftl">
<#-- @ftlvariable name="pool" type="org.atlast.beans.Land" -->

<h3>Labour Pool</h3>

<div>
  <p>

      <#list pool.pops as pop>
        <form action="<@hst.actionURL/>" method="post">
          <span><input type="hidden" value="${pop.uuid}" name="uuid"><input type="hidden" value="hire" name="action"><input class="button ${pop.popClass}" type="submit" value="${pop.name}&nbsp;&#9650;"></span>
        </form>
      </#list>

  </p>
</div>