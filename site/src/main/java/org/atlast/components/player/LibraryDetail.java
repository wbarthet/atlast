package org.atlast.components.player;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.atlast.beans.Identity;
import org.atlast.beans.Library;
import org.atlast.beans.Market;
import org.atlast.beans.Player;
import org.atlast.beans.Pop;
import org.atlast.components.world.BaseSecuredComponent;
import org.atlast.services.CouncilService;
import org.atlast.services.LandsService;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class LibraryDetail extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(LibraryDetail.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        HstRequestContext requestContext = request.getRequestContext();
        Library library = requestContext.getContentBean(Library.class);

        Player player = library.getPlayer();

        request.setAttribute("library", library);
        request.setAttribute("player", player);

        try {
            Node poolNode = requestContext.getSiteContentBaseBean().getNode().getNode("worlddata/pool");

            HstQuery query = requestContext.getQueryManager().createQuery(poolNode, Pop.class);

            if ("alignment".equals(getPublicRequestParameter(request, "sort"))) {
                Identity identity = player.getIdentity();
                String identityType = identity.isRace() ? "race" : "religion";

                query.addOrderByDescending(identityType + "-" + identity.getUuid());
            } else {
                query.addOrderByDescending("skill-scientist");
            }
            query.setLimit(10);

            HstQueryResult hstQueryResult = query.execute();

            request.setAttribute("pool", hstQueryResult.getHippoBeans());

        } catch (RepositoryException e) {
            log.error("Error retrieving worker pool");
        } catch (QueryException e) {
            log.error("Error retrieving workers");
        }
    }


    @Persistable
    @Override
    public void doAction(final HstRequest request, final HstResponse response) throws HstComponentException {

        String type = getPublicRequestParameter(request, "action");
        String uuid = getPublicRequestParameter(request, "uuid");
        CouncilService councilService = HstServices.getComponentManager().getComponent(CouncilService.class);
        if (securityCheck(request, uuid)) {
            final HstRequestContext requestContext = request.getRequestContext();
            if ("fire".equals(type)) {
                try {
                    councilService.fire(requestContext, uuid);
                } catch (RepositoryException e) {
                    log.error("Error firing pop from land", e);
                }
            } else if ("hire".equals(type)) {
                try {
                    String player = request.getRequestContext().getServletRequest().getUserPrincipal().getName();
                    councilService.hire(request.getRequestContext(), uuid, player);
                } catch (RepositoryException e) {
                    log.error("Error updating labour pool", e);
                }

            } else if ("save".equals(type)) {

                try {
                    Double wages = Double.parseDouble(getPublicRequestParameter(request, "wages"));

                    String focus = getPublicRequestParameter(request, "focus");

                    councilService.save(requestContext, uuid, wages, focus);

                } catch (RepositoryException e) {
                    log.error("Error updating land", e);
                } catch (ObjectBeanManagerException e) {
                    log.error("Error updating land", e);
                }
            }
        }
    }
}
