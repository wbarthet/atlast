package org.atlast.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.commons.lang.StringUtils;
import org.atlast.services.LandsService;
import org.atlast.services.SignupService;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.component.support.forms.FormUtils;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/20/15.
 */
public class Signup extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(Signup.class);


    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        FormMap map = new FormMap(request, new String[]{"userName", "password"});
        FormUtils.populate(request, map);

        List<String> messages = new ArrayList<>();
        for (String field : map.getFieldNames()) {
            for (String message : map.getField(field).getMessages()) {
                messages.add(message);
            }
        }

        request.setAttribute("messages", messages);
    }

    @Override
    @Persistable
    public void doAction(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doAction(request, response);

        String userName = getPublicRequestParameter(request, "userName");
        String password = getPublicRequestParameter(request, "password");
        String password2 = getPublicRequestParameter(request, "password2");
        boolean robot = Boolean.parseBoolean(getPublicRequestParameter(request, "amrobot"));
        boolean notRobot = Boolean.parseBoolean(getPublicRequestParameter(request, "amnotrobot"));

        boolean valid = true;

        FormMap map = new FormMap(request, new String[]{"userName", "password"});

        valid &= StringUtils.isNotBlank(userName);
        valid &= StringUtils.isNotEmpty(userName);
        if (!valid) {
            map.addMessage("userName", "user name cannot be blank");
        } else {
            valid &= StringUtils.isNotEmpty(password);
            valid &= StringUtils.isNotBlank(password);
            if (!valid) {
                map.addMessage("password", "password cannot be blank");
            } else {
                valid &= StringUtils.isNotEmpty(password2);
                valid &= StringUtils.isNotBlank(password2);
                if (!valid) {
                    map.addMessage("password", "password cannot be blank");
                } else {
                    valid &= password.equals(password2);
                    if (!valid) {
                        map.addMessage("password", "password must match");
                    } else {
                        valid &= robot;
                        valid &= !notRobot;
                        if (!valid) {
                            map.addMessage("userName", "robots aren't allowed to play");
                        }
                    }
                }
            }
        }


        SignupService signupService = HstServices.getComponentManager().getComponent(SignupService.class);

        if (valid) {
            try {
                HstRequestContext requestContext = request.getRequestContext();

                String success = signupService.signup(requestContext, userName, password);

                map.addMessage("userName", success);

            } catch (RepositoryException e) {
                map.addMessage("userName", "Error creating user account");
                log.error("Error creating user account", e);
            } catch (QueryException e) {
                map.addMessage("userName", "Error creating user account");
                log.error("Error creating user account", e);
            }
        }


        FormUtils.persistFormMap(request, response, map, null);
    }
}
