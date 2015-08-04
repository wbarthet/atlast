package org.atlast.components.player;

import org.atlast.beans.Player;
import org.atlast.beans.Pop;
import org.atlast.components.world.BaseSecuredComponent;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class PopsOverview extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(PopsOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        Player player = request.getRequestContext().getContentBean(Player.class);


        try {
            HstQuery query = request.getRequestContext().getQueryManager().createQuery(player, Pop.class);

            query.setLimit(100);

            HstQueryResult result = query.execute();

            request.setAttribute("pops", result.getHippoBeans());

        } catch (QueryException e) {
            log.error("Error getting pops", e);
        }


    }
}
