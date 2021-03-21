package com.anup.app;

import com.anup.app.component.PersonCrudComponent;
import com.anup.app.di.Injector;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
public class UserAccountApplication {
    public static void main(String[] args) {
        Injector.startApplication(UserAccountApplication.class);
        Injector.getService(PersonCrudComponent.class).crud();
    }
}
