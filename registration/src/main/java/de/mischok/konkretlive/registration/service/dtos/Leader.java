package de.mischok.konkretlive.registration.service.dtos;

import org.hibernate.validator.constraints.NotBlank;

public class Leader extends Person {

	@NotBlank
	private String street;

	@NotBlank
	private String zipcode;

	@NotBlank
	private String city;

	private String addressextra;
	
	public Leader() {}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the addressextra
	 */
	public String getAddressextra() {
		return addressextra;
	}

	/**
	 * @param addressextra the addressextra to set
	 */
	public void setAddressextra(String addressextra) {
		this.addressextra = addressextra;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
}
