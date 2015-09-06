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
                    <h4>
                      <img class="land-icon forest" src="<@hst.link hippobean=land.landDescriptor.icon.mediumicon/>"/> ${land.name}
                    </h4>
                    <p>empty land</p>
                  </div>
                </div>
              <#else>
                <div class="small-12 medium-4 large-4 columns">
                  <div class="feature-box">
                    <h4>
                      <img class="land-icon forest" src="<@hst.link hippobean=land.landDescriptor.icon.mediumicon/>"/> ${land.name}
                    </h4>
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
                                  <img class="resource-icon" src="img/goods/flour.png"/>
                                </#list>

                            </#if>

                          OUT: 7 x <img class="resource-icon" src="img/goods/bread.png"/>

                          ($123.45)

                        </#if>
                    </p>
                  </div>
                </div>

              </#if>

          </#if>
      </#list>


  </div>
</#list>

  <div class="small-12 medium-4 large-4 columns">
    <div class="feature-box">
      <h4><img class="land-icon" src="img/lands/lightforest.png"/> Maddraquk Woodlands</h4>
      <p><img class="land-icon" src="img/developments/camp.png"/>Camp <span class="recipe-box">Chop Wood</span>
      </p>
      <p>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" style="background-color: gold" src="img/other/lower2.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-iconinitial commit" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/lower.png"/>
        <img class="pop-icon" src="img/other/middle.png"/>
        <img class="pop-icon" src="img/other/upper.png"/>
      </p>
      <p>
        OUT: 38 x <img class="resource-icon" src="img/goods/logs.png"/>

        $123.45
      </p>
    </div>

  </div>
  <div class="small-12 medium-4 large-4 columns">
    <div class="feature-box">
      <h4><img class="land-icon" src="img/lands/grassland.png"/> Maddraquk Meadows</h4>
      <p><img class="land-icon" src="img/developments/furnace.png"/>Furnace
        <span class="recipe-box">Bake Bread</span>
      </p>
      <p>
        <img class="pop-icon" src="img/other/middle.png"/>
        <img class="pop-icon" src="img/other/middle.png"/>
        <img class="pop-icon" src="img/other/upper.png"/>
      </p>
      <p>
        IN: 3 x <img class="resource-icon" src="img/goods/flour.png"/>
        OUT: 7 x <img class="resource-icon" src="img/goods/bread.png"/>

        ($123.45)
      </p>
    </div>
  </div>

</div>
