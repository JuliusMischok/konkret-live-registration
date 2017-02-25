package de.mischok.konkretlive.registration.test;

import org.springframework.context.annotation.Import;

import de.mischok.konkretlive.registration.KonkretLiveRegistrationApplication;

/**
 * Configuration using an embedded instance of Neo4j for testing purposes
 */
@org.springframework.context.annotation.Configuration
@Import(KonkretLiveRegistrationApplication.class)
public class TestConfiguration {
	
}