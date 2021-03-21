package com.anup.app.component;

import com.anup.app.annotation.Autowired;
import com.anup.app.annotation.Component;
import com.anup.app.model.Person;
import com.anup.app.service.PersonService;

import java.util.List;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
@Component
public class PersonCrudComponent {

    @Autowired
    private PersonService personService;

    public void crud() {
        personService.save(new Person(1L, "Anup"));
        personService.save(new Person(2L, "Laxman"));
        personService.save(new Person(3L, "Birendra"));
        personService.save(new Person(4L, "Manjit"));
        personService.save(new Person(5L, "Ram"));

        personService.update(new Person(2L, "Lux"));

        final List<Person> persons = personService.findAll();

        for (Person person : persons) {
            System.out.println(person);
        }
        System.out.println("personService.findById(4L): " + personService.findById(4L));

    }

}
