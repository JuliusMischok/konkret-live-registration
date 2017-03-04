package de.mischok.konkretlive.registration.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Leader extends Person {
	
	@NotBlank
	private String street;

	@NotBlank
	private String zipcode;

	@NotBlank
	private String city;

	private String addressextra;
	
	private boolean counselling;
	
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

	/**
	 * @return the counseling
	 */
	public boolean isCounselling() {
		return counselling;
	}

	/**
	 * @param counseling the counseling to set
	 */
	public void setCounselling(boolean counseling) {
		this.counselling = counseling;
	}
}
