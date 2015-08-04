package org.atlast.components.world;

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.atlast.beans.Market;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.configuration.sitemap.HstSiteMap;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class BaseSecuredComponent extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(BaseSecuredComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {


        String userName = getComponentParameter("userName");

        final HstRequestContext requestContext = request.getRequestContext();
        if (!userName.equals(requestContext.getServletRequest().getUserPrincipal().getName())) {
            response.setStatus(401);

            final ResolvedSiteMapItem resolvedSiteMapItem = RequestContextProvider.get().getResolvedSiteMapItem();
            final HstSiteMap siteMap = resolvedSiteMapItem.getHstSiteMapItem().getHstSiteMap();
            final HstSiteMapItem loginPage = siteMap.getSiteMapItemByRefId("login-form");

            String link = loginPage.getValue();
            try {
                response.forward('/' + link);
            } catch (IOException e) {
                log.error("Error forwarding to login form", e);
            }

            return;
        }


    }

    protected  boolean securityCheck(final HstRequest request, String uuid) {

        boolean clear = false;

        try {
            String player = request.getRequestContext().getSession().getNodeByIdentifier(uuid).getProperty("atlast:player").getString();

            String user = request.getRequestContext().getServletRequest().getUserPrincipal().getName();

            clear = player.equals(user);
        } catch (RepositoryException e) {
            log.error("Could not process action, security violation!");
        }

        return clear;
    }

}
