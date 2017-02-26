package de.mischok.konkretlive.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.mischok.konkretlive.registration.model.Person;

public interface PersonRepository extends MongoRepository<Person, String> {

}
