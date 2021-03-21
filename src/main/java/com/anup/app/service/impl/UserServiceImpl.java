package com.anup.app.service.impl;

import com.anup.app.annotation.Component;
import com.anup.app.service.UserService;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
@Component
public class UserServiceImpl implements UserService {

    @Override
    public String getUsername() {
        return "username";
    }
}
