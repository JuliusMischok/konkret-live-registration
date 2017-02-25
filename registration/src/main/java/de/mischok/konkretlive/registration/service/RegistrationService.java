package de.mischok.konkretlive.registration.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.mischok.konkretlive.registration.service.dtos.Registration;

/**
 * Service implementation 
 */
@Controller
@RequestMapping("registration")
public class RegistrationService {
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveRegistration(@RequestBody @Valid Registration registration, BindingResult bindingResult) {
		Assert.notNull(registration);
		Assert.notNull(bindingResult);
		
		// FIXME: Implementierung
		
		throw new RuntimeException("Not yet implemented");
	}
}
