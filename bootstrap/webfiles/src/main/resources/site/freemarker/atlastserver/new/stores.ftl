<#include "../../include/imports.ftl">
<#-- @ftlvariable name="stores" type="java.util.List<org.atlast.beans.Market>" -->
<#-- @ftlvariable name="storesService" type="org.atlast.services.StoresService>" -->


<div class="section-headline">
  <div class="row">
    <div class="small-12 medium-12 large-12 large-centered columns">
      <h1>Stores</h1>
    </div>
  </div>
</div>

<div class="section-features section-bg-color1">

  <form action="<@hst.actionURL/>" method="post" oninput="<#list stores.stores as store><#list store.items as item>${item}amount.value=${item}range.value; </#list></#list>">
  <#list stores.stores?chunk(3) as storerow>
    <div class="row">
        <#list storerow as store>
          <div class="small-12 medium-4 large-4 columns">
            <div class="feature-box">
              <h4><i class="fa icon-${store.name} fa-lg fa-fw"></i> ${store.name?cap_first}</h4>
              <br>
                <#list store.items as item>

                  <p>
                    <#if storesService.getResource(item?cap_first).icon??>
                      <img src="<@hst.link hippobean=storesService.getResource(item?cap_first).icon.smallicon/>"/>
                    </#if>
                    <input name="${item}range" id="${item}range" type="range" min="0" max="100" step="1" value="${store.getResourceLevel(item)}">

                  ${item?cap_first} ${store.getResource(item)}/
                    <output name="${item}amount" for="${item}range">${store.getResourceLevel(item)}</output>
                    &nbsp;($34.43)
                  </p>
                </#list>
            </div>
          </div>
        </#list>
    </div>


  </#list>

    <input type="hidden" name="uuid" value="${stores.uuid}">
    <input type="hidden" name="type" value="save">

    <div class="row-centered">
      <input value="save" type="submit" class="button round">
    </div>

  </form>
</div>
