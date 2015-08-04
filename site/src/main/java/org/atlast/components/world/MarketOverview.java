package org.atlast.components.world;

import org.atlast.beans.Market;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class MarketOverview extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(MarketOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        Market market = request.getRequestContext().getContentBean(Market.class);

        request.setAttribute("stores", market.getStores());


    }
}
