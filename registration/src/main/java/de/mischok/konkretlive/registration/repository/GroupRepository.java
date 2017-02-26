package de.mischok.konkretlive.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.mischok.konkretlive.registration.model.Group;

public interface GroupRepository extends MongoRepository<Group, String> {

}
