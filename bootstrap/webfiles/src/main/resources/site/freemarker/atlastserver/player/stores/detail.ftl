<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="store" type="org.atlast.beans.Store" -->



<div id="page">

  <!-- Main -->
  <div id="main" class="container">
    <div class="row">
      <div class="12u">
        <section>
          <header>
            <h2>${store.name}</h2>
          </header>
        </section>
      </div>
      <form action="<@hst.actionURL />" method="post">
        <ul>
        <#list store.items as item>
          <li>
          ${item}: ${store.getResource(item)}/${store.getResourceLevel(item)}
            <input name="${item}-level" value="${store.getResourceLevel(item)}" type="number">
            <input type="hidden" value="${store.uuid}" name="uuid"/>
            <input type="hidden" value="save" name="type"/>
          </li>
        </#list>
        </ul>
        <input class="button" type="submit" value="Save">
      </form>
    </div>
  </div>

</div>