package org.cboard.cboardservice.modules.services;

import org.cboard.cboardservice.modules.dto.User;

/**
 * Created by yfyuan on 2016/9/29.
 */
public interface AuthenticationService {

    User getCurrentUser();
}
