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
        <div class="small-12 medium-4 large-4 columns">
          <div class="feature-box">
            <h4><img class="land-icon forest" src="img/lands/lightforest.png"/> ${land.name}</h4>
            <p>Empty Land</p>
          </div>
        </div>
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
        <img class="pop-icon" src="img/other/lower.png"/>
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
