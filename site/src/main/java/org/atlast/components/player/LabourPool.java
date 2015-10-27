package org.atlast.components.player;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.atlast.beans.AtlastObject;
import org.atlast.beans.Land;
import org.atlast.components.world.BaseSecuredComponent;
import org.atlast.services.LandsService;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class LabourPool extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(LabourPool.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        AtlastObject object = request.getRequestContext().getContentBean(AtlastObject.class);

        if (object instanceof Land) {
            object = (AtlastObject) object.getParentBean();
        }

        HippoBean pool = object.getBean("pool");

        request.setAttribute("pool", pool);
    }

    @Persistable
    @Override
    public void doAction(final HstRequest request, final HstResponse response) throws HstComponentException {

        String type = getPublicRequestParameter(request, "action");
        String uuid = getPublicRequestParameter(request, "uuid");
        LandsService landsService = HstServices.getComponentManager().getComponent(LandsService.class);
        if (securityCheck(request, uuid)) {
            if ("hire".equals(type)) {
                try {
                   landsService.hire(request.getRequestContext(), uuid, "global");
                } catch (RepositoryException e) {
                    log.error("Error updating labour pool", e);
                } catch (ObjectBeanManagerException e) {
                    log.error("Error updating labour pool", e);
                }

            }
        }
    }
}
