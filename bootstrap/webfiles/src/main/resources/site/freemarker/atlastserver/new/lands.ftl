<#include "../../include/imports.ftl">
<#-- @ftlvariable name="lands" type="java.util.List<org.atlast.beans.Land>" -->

<#-- @ftlvariable name="worldMarket" type="org.atlast.beans.Market" -->

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
                          <img class="pop-icon" style="background-color: ${pop.identity.colour};" src="<@hst.link path="binaries/content/gallery/atlastserver/img/lower2.png"/>"/>
                        </#list>
                    </p>

                    <p>
                        <#if land.recipeDescriptor??>
                            <#if land.pops?has_content>

                              <p>
                                Skill level ${land.getSkillLevel(land.recipeDescriptor.skill)}% | Tech level: 43% | Land exhaustion: 67%
                              </p>
                              <p>
                                  <#assign pnl=0.00/>
                                  <#if land.recipeDescriptor.inputs?has_content>
                                    Input:
                                      <#list land.recipeDescriptor.inputs as input>
                                          <#assign inputCount = input.quantity * land.pops?size / land.recipeDescriptor.labour/>
                                      ${inputCount} x ${input.resourceDescriptor.name}
                                        <img class="resource-icon" src="<@hst.link hippobean=input.resourceDescriptor.icon.smallicon/>"/>
                                          <#assign pnl=pnl-inputCount * worldMarket.getStore(input.resourceDescriptor.category?lower_case).getResourceLevel(input.resourceDescriptor.name?lower_case)?double/>
                                      </#list>
                                    |</#if>
                                  <#assign pnl = pnl - (land.wages * land.pops?size)/>

                                  <#if land.recipeDescriptor.outputs?has_content>
                                    Output:
                                      <#list land.recipeDescriptor.outputs as output>
                                      ${output.resourceDescriptor.name} x ${land.outputs[output.resourceDescriptor.name]}
                                      <#-- ${land.getOutputs().get(output.name)} x ${output.name}-->
                                        <img class="resource-icon" src="<@hst.link hippobean=output.resourceDescriptor.icon.smallicon/>"/>
                                          <#assign pnl=pnl + land.outputs[output.resourceDescriptor.name] * worldMarket.getStore(output.resourceDescriptor.category?lower_case).getResourceLevel(output.resourceDescriptor.name?lower_case) />
                                      </#list>
                                  </#if>
                                |
                              ${pnl?string.currency}
                              </p>
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
