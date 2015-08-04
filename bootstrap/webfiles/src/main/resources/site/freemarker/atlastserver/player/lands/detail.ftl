<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="land" type="org.atlast.beans.Land" -->

<div id="page">

  <!-- Main -->
  <div id="main" class="container">
    <div class="row">
      <div class="12u">
        <section>
          <header>
            <h2>${land.name}</h2>
          </header>

          <span class="byline">
          ${land.landDescriptor.name} <#if land.development> - ${land.developmentDescriptor.name}</#if>
          </span>

        </section>
      </div>
    </div>
  <#if land.development>

    <div class="row">
      <div class="9u">
        <form method="post" action="<@hst.actionURL/>">
          <input type="hidden" name="uuid" value="${land.uuid}">
          <input type="hidden" name="action" value="destroy">
          <input class="button" type="submit" value="Destroy development">
        </form>
      </div>
    </div>

    <div class="row">
      <div class="9u">
        <form method="post" action="<@hst.actionURL/>">
          <p>Wages: <input type="number" name="wages" value="${land.wages}"></p>

          <p>
            <select name="recipe">
                <#if land.recipeDescriptor??>
                  <option value="none" selected>Do nothing</option>
                <#else>
                  <option value="none">Do nothing</option>
                </#if>

                <#list land.developmentDescriptor.allowedRecipes as recipe>
                    <#if land.recipeDescriptor?? && land.recipeDescriptor.uuid == recipe.uuid>
                      <option value="${recipe.uuid}" selected>${recipe.name}</option>
                    <#else >
                      <option value="${recipe.uuid}">${recipe.name}</option>
                    </#if>
                </#list>
            </select>
          </p>
          <input type="hidden" name="uuid" value="${land.uuid}">
          <input type="hidden" name="action" value="save">

          <input class="button" type="submit" value="Save">
        </form>
      </div>
    </div>

    <div class="row">
      <div class="9u">
          <#list land.pops as pop>
            <form action="<@hst.actionURL/>" method="post">
              <span><input type="hidden" value="${pop.uuid}" name="uuid"><input type="hidden" value="fire" name="action"><input class="button ${pop.popClass}" type="submit" value="${pop.name}&nbsp;&#9660;"></span>
            </form>
          </#list>
      </div>
    </div>
    <div class="row">
      <div class="9u">
          <@hst.include ref="bottom"/>
      </div>
    </div>
  <#else>
    <div class="row">
      <div class="9u">

        <form method="post" action="<@hst.actionURL/>">
          <select name="development">
            <option value="empty">Empty Land</option>
              <#list land.getLandDescriptor().allowedDevelopments as development>

                <option value="${development.uuid}">${development.name}</option>

              </#list>
          </select>
          <input type="hidden" name="uuid" value="${land.uuid}">
          <input type="hidden" name="action" value="build">
          <input class="button" type="submit" value="Build">
        </form>

      </div>
    </div>


  </#if>

  </div>
</div>

</div>
