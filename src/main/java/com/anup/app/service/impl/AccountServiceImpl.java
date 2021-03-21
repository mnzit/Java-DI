package com.anup.app.service.impl;

import com.anup.app.annotation.Component;
import com.anup.app.annotation.Qualifer;
import com.anup.app.service.AccountService;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
@Component
@Qualifer("impl1")
public class AccountServiceImpl implements AccountService {

    @Override
    public Long getAccountNumber(String username) {
        return 12345678L;
    }
}
