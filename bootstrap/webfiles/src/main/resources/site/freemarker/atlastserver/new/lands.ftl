<#include "../../include/imports.ftl">
<#-- @ftlvariable name="lands" type="java.util.List<org.atlast.beans.Land>" -->


<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>Lands</h1>
      <h5>In the realm of the Aqurids</h5>
    </div>
  </div>
</div>

<div class="section-features section-bg-color1">
<#list lands?chunk(3) as landrow>
  <div class="row">
      <#list landrow as land>
          <#if land.name != "pool">
              <#if !land.development>
                <div class="small-12 medium-4 large-4 columns">
                  <div class="feature-box">
                    <a href="<@hst.link hippobean=land/>">
                      <h4>
                        <img class="land-icon forest" src="<@hst.link hippobean=land.landDescriptor.icon.mediumicon/>"/> ${land.name}
                      </h4>
                    </a>
                    <p>empty land</p>
                  </div>
                </div>
              <#else>
                <div class="small-12 medium-4 large-4 columns">
                  <div class="feature-box">
                    <a href="<@hst.link hippobean=land/>">
                      <h4>
                        <img class="land-icon forest" src="<@hst.link hippobean=land.landDescriptor.icon.mediumicon/>"/> ${land.name}
                      </h4>
                    </a>
                    <p>
                      <img class="land-icon" src="<@hst.link hippobean=land.developmentDescriptor.icon/>"/>${land.developmentDescriptor.name}
                      <span class="recipe-box">${land.recipeDescriptor.name}</span></p>
                    <p>
                        <#list land.pops as pop>
                          <img class="pop-icon" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower.png"/>"/>
                        </#list>
                    </p>

                    <p>
                        <#if land.recipeDescriptor??>
                            <#if land.recipeDescriptor.inputs?has_content>
                              IN: ${land.pops?size} x
                                <#list land.recipeDescriptor.inputs as input>
                                  <img class="resource-icon" src="<@hst.link hippobean=input.resourceDescriptor.icon.smallicon/>"/>
                                </#list>

                            </#if>

                            <#if land.recipeDescriptor.outputs?has_content>
                              OUT: 7 x
                                <#list land.recipeDescriptor.outputs as output>
                                  <img class="resource-icon" src="<@hst.link hippobean=output.resourceDescriptor.icon.smallicon/>"/>
                                </#list>

                              ($123.45)
                            </#if>

                        </#if>
                    </p>
                  </div>
                </div>

              </#if>

          </#if>
      </#list>


  </div>
</#list>
</div>
