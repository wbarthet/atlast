package org.atlast.components.player;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.atlast.beans.Identity;
import org.atlast.beans.Market;
import org.atlast.beans.Player;
import org.atlast.beans.Pop;
import org.atlast.beans.Store;
import org.atlast.beans.Storehouse;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.components.world.BaseSecuredComponent;
import org.atlast.services.CouncilService;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
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
public class StorehouseDetail extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(StorehouseDetail.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        HstRequestContext requestContext = request.getRequestContext();
        Storehouse storehouse = requestContext.getContentBean(Storehouse.class);

        Player player = storehouse.getPlayer();

        request.setAttribute("storehouse", storehouse);
        request.setAttribute("player", player);

        Market worldMarket = requestContext.getSiteContentBaseBean().getBean("worlddata/market");

        request.setAttribute("worldMarket", worldMarket);

        try {
            Node poolNode = requestContext.getSiteContentBaseBean().getNode().getNode("worlddata/pool");

            HstQuery query = requestContext.getQueryManager().createQuery(poolNode, Pop.class);

            if ("alignment".equals(getPublicRequestParameter(request, "sort"))) {
                Identity identity = player.getIdentity();
                String identityType = identity.isRace() ? "race" : "religion";

                query.addOrderByDescending(identityType + "-" + identity.getUuid());
            } else {
                query.addOrderByDescending("skill-merchant");
            }
            query.setLimit(10);

            HstQueryResult hstQueryResult = query.execute();

            request.setAttribute("pool", hstQueryResult.getHippoBeans());

        } catch (RepositoryException e) {
            log.error("Error retrieving worker pool");
        } catch (QueryException e) {
            log.error("Error retrieving workers");
        }



        try {
            HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSiteContentBaseBean(), RecipeDescriptor.class);
            Filter filter = query.createFilter();

            filter.addEqualTo("atlast:skill", "merchant");

            query.setFilter(filter);
            HstQueryResult hstQueryResult = query.execute();
            request.setAttribute("recipes", hstQueryResult.getHippoBeans());
        } catch (QueryException e) {
            log.error("Error retrieving recipes");
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

                    String recipe = getPublicRequestParameter(request, "recipe");

                    councilService.saveStorehouse(requestContext, uuid, wages, recipe);

                } catch (RepositoryException e) {
                    log.error("Error updating land", e);
                } catch (ObjectBeanManagerException e) {
                    log.error("Error updating land", e);
                }
            }
        }
    }
}
