package de.mischok.konkretlive.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.mischok.konkretlive.registration.model.Staff;

public interface StaffRepository extends MongoRepository<Staff, String> {

}
