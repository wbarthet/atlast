<#include "../../../include/imports.ftl">
<#-- @ftlvariable name="lands" type="java.util.List<org.atlast.beans.Land>" -->


<div id="page">

  <!-- Main -->
  <div id="main" class="container">
    <div class="row">
      <div class="12u">
        <section>
          <header>
            <h2>Lands and Developments</h2>
          </header>
        </section>
      </div>
      <p></p>
      <p></p>
    </div>
  </div>
  <!-- Main -->

  <div id="marketing" class="container">
  <#list lands?chunk(3) as row>

    <div class="row">
        <#list row as land>
            <#if land.name != "pool">
              <div class="3u">
                <section>
                  <header>
                    <h2><a href="<@hst.link hippobean=land/>">${land.name}</a></h2>
                  </header>
                  <p class="subtitle">${land.landDescriptor.name}<#if land.development> - ${land.developmentDescriptor.name}</#if></p>
                  <p><a href="<@hst.link hippobean=land/>"><img src="images/pics13.jpg" alt=""></a></p>
                    <#if land.development>
                      <p>${land.recipeDescriptor.name}</p>
                        <#list land.pops?chunk(3) as row>
                          <p>
                              <#list row as pop>
                                <span>${pop.name}</span>
                              </#list>
                          </p>
                        </#list>
                    </#if>
                  <p><a href="<@hst.link hippobean=land/>" class="button">More</a></p>
                </section>
              </div>
            </#if>
        </#list>
    </div>

  </#list>
  </div>
</div>
