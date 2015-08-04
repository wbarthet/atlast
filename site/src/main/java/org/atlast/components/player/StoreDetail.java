package org.atlast.components.player;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.atlast.beans.Store;
import org.atlast.components.world.BaseSecuredComponent;
import org.atlast.services.StoresService;
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
public class StoreDetail extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(StoreDetail.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        Store store = request.getRequestContext().getContentBean(Store.class);

        request.setAttribute("store", store);
    }

    @Persistable
    @Override
    public void doAction(final HstRequest request, final HstResponse response) throws HstComponentException {

        String uuid = getPublicRequestParameter(request, "uuid");
        StoresService storesService = HstServices.getComponentManager().getComponent(StoresService.class);
        if (securityCheck(request, uuid)) {
            final HstRequestContext requestContext = request.getRequestContext();
            if ("save".equals(getPublicRequestParameter(request, "type"))) {
                //TODO space validation


                try {
                    Map<String, Double> levels = new HashMap<>();
                    Session session = requestContext.getSession();
                    final Node storeNode = session.getNodeByIdentifier(uuid);

                    Value[] items = storeNode.getProperty("atlast:items").getValues();

                    for (Value item : items) {
                        Double level = Double.parseDouble(getPublicRequestParameter(request, item.getString() + "-level"));

                        levels.put(item.getString(), level);
                    }

                    storesService.save(requestContext, uuid, levels);
                } catch (RepositoryException e) {
                    log.error("Error updating store", e);
                } catch (ObjectBeanManagerException e) {
                    log.error("Error updating store", e);
                }


            }
        }
    }
}
