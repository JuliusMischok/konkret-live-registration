package de.mischok.konkretlive.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.mischok.konkretlive.registration.model.Leader;

public interface LeaderRepository extends MongoRepository<Leader, String> {

}
