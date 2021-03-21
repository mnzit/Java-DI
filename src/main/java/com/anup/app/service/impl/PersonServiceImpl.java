package com.anup.app.service.impl;

import com.anup.app.annotation.Component;
import com.anup.app.model.Person;
import com.anup.app.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
@Component
public class PersonServiceImpl implements PersonService {

    static List<Person> personList = null;

    static {
        personList = new ArrayList<>();
    }

    @Override
    public List<Person> findAll() {
        return personList;
    }

    @Override
    public Person findById(Long id) {
        return personList
                .stream()
                .filter(person -> person.getId().longValue() == id.longValue())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Person person) {
        personList.add(person);
    }

    @Override
    public void update(Person person) {
        personList = personList
                .stream()
                .map(p -> {
                    if (person.getId().longValue() == p.getId().longValue()) {
                        p = person;
                    }
                    return p;
                }).collect(Collectors.toList());
    }
}
