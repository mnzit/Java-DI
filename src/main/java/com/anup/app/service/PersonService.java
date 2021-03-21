package com.anup.app.service;

import com.anup.app.model.Person;

import java.util.List;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
public interface PersonService {

    List<Person> findAll();

    Person findById(Long id);

    void save(Person person);

    void update(Person person);
}
