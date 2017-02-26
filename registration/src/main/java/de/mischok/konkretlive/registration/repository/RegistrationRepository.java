package de.mischok.konkretlive.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.mischok.konkretlive.registration.model.Registration;

public interface RegistrationRepository extends MongoRepository<Registration, String> {

}
