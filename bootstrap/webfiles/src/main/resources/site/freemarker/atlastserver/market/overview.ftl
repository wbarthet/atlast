<#include "../../include/imports.ftl">
<#-- @ftlvariable name="stores" type="java.util.List<org.atlast.beans.Store>" -->

<div id="page">

  <!-- Main -->
  <div id="main" class="container">
    <div class="row">
      <div class="12u">
        <section>
          <header>
            <h2>World Markets</h2>
          </header>
        </section>
      </div>
      <p></p>
      <p></p>
    </div>
  </div>
  <!-- Main -->

  <div id="marketing" class="container">


  <#list stores?chunk(3) as row>
  <div class="row">
    <#list row as store>

          <div class="3u">
            <section>
              <header>
                <h2>${store.name}</h2>
              </header>
              <p><a href="#"><img src="images/pics13.jpg" alt=""></a></p>
                <#list store.items as item>
                  <p class="subtitle">${item}:&nbsp; ${store.getResourceLevel(item)?string.currency}</p>
                </#list>

            </section>
          </div>

      </#list>
  </div>
  </#list>



  </div>
</div>


