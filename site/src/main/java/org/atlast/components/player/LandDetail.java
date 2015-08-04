package org.atlast.components.player;

import javax.jcr.RepositoryException;

import org.atlast.beans.Land;
import org.atlast.components.world.BaseSecuredComponent;
import org.atlast.services.LandsService;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
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
public class LandDetail extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(LandDetail.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        Land land = request.getRequestContext().getContentBean(Land.class);

        request.setAttribute("land", land);


    }

    @Persistable
    @Override
    public void doAction(final HstRequest request, final HstResponse response) throws HstComponentException {

        String type = getPublicRequestParameter(request, "action");
        String uuid = getPublicRequestParameter(request, "uuid");
        LandsService landsService = HstServices.getComponentManager().getComponent(LandsService.class);
        if (securityCheck(request, uuid)) {
            final HstRequestContext requestContext = request.getRequestContext();
            if ("fire".equals(type)) {
                try {
                    landsService.fire(requestContext, uuid);
                } catch (RepositoryException e) {
                    log.error("Error firing pop from land", e);
                }
            } else if ("save".equals(type)) {

                try {
                    Double wages = Double.parseDouble(getPublicRequestParameter(request, "wages"));

                    String recipeUuid = getPublicRequestParameter(request, "recipe");

                    landsService.save(requestContext, uuid, wages, recipeUuid);

                } catch (RepositoryException e) {
                    log.error("Error updating land", e);
                } catch (ObjectBeanManagerException e) {
                    log.error("Error updating land", e);
                }
            } else if ("build".equals(type)) {
                try {
                    String developmentDescriptorUuid = getPublicRequestParameter(request, "development");


                    landsService.build(requestContext, uuid, developmentDescriptorUuid);

                } catch (RepositoryException e) {
                    log.error("Error building on land", e);
                }

            } else if ("destroy".equals(type)) {
                try {
                    landsService.destroy(requestContext, uuid);

                } catch (RepositoryException e) {
                    log.error("Error destroying building on land", e);
                }


            }
        }
    }
}
