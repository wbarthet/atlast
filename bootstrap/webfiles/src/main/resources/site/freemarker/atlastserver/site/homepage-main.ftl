<#include "../../include/imports.ftl">

<#-- @ftlvariable name="document" type="org.atlast.beans.ContentDocument" -->

  <div id="page">

    <!-- Main -->
    <div id="main" class="container">
      <div class="row">
        <div class="12u">
          <section>
            <header>
              <h2>${document.title}</h2>
              <span class="byline">Augue praesent a lacus at urna congue rutrum</span>
            </header>
          <@hst.html hippohtml=document.content/>
          </section>
        </div>

      </div>
    </div>
    <!-- Main -->

  </div>


