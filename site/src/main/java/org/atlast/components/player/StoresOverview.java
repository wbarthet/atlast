package org.atlast.components.player;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.atlast.beans.Market;
import org.atlast.components.world.BaseSecuredComponent;
import org.atlast.services.StoresService;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
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
public class StoresOverview extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(StoresOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        Market market = request.getRequestContext().getContentBean(Market.class);

        request.setAttribute("stores", market);


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

                    final Node storesNode = session.getNodeByIdentifier(uuid);

                    NodeIterator stores = storesNode.getNodes();

                    while(stores.hasNext()) {

                        Node storeNode = stores.nextNode();

                        Value[] items = storeNode.getProperty("atlast:items").getValues();

                        for (Value item : items) {
                            Double level = Double.parseDouble(getPublicRequestParameter(request, item.getString() + "rangea "));

                            levels.put(item.getString(), level);
                        }

                        storesService.save(requestContext, storeNode.getIdentifier(), levels);
                    }
                } catch (RepositoryException e) {
                    log.error("Error updating store", e);
                } catch (ObjectBeanManagerException e) {
                    log.error("Error updating store", e);
                }


            }
        }
    }
}
