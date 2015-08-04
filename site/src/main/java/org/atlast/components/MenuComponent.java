package org.atlast.components;

import java.security.Principal;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.onehippo.cms7.essentials.components.EssentialsMenuComponent;


/**
 * Created by wbarthet on 7/16/15.
 */
@ParametersInfo(type = MenuComponentInfo.class)
public class MenuComponent extends EssentialsMenuComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final HstRequestContext requestContext = request.getRequestContext();
        final Principal player = requestContext.getServletRequest().getUserPrincipal();
        if (player != null) {

            final MenuComponentInfo paramInfo = getComponentParametersInfo(request);

            HstSiteMenu secureMenu = requestContext.getHstSiteMenus().getSiteMenu(paramInfo.getSecureMenu());

            request.setAttribute("secureMenu", secureMenu);
            request.setAttribute("player", player.getName());
        }

    }
}
