package com.anup.app.component;

import com.anup.app.annotation.Autowired;
import com.anup.app.annotation.Component;
import com.anup.app.annotation.Qualifer;
import com.anup.app.service.AccountService;
import com.anup.app.service.UserService;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
@Component
public class UserAccountClientComponent {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifer(value = "accountServiceImpl")
    private AccountService accountService;

    public void displayUserAccount() {
        String username = userService.getUsername();
        Long accountNumber = accountService.getAccountNumber(username);
        System.out.println("username : " + username);
        System.out.println("accountNo : " + accountNumber);
    }

}
