<#include "../../include/imports.ftl">
<#-- @ftlvariable name="player" type="org.atlast.beans.Player" -->

<div id="page">

  <!-- Main -->
  <div id="main" class="container">
    <div class="row">

    <@hst.include ref="left"/>

      <div class="9u skel-cell-important">
        <section>
          <header>
            <h2>${player.name}</h2>
          </header>

          <p>Cash: ${player.cash}</p>
        </section>
      </div>

    </div>
  </div>
  <!-- Main -->

</div>