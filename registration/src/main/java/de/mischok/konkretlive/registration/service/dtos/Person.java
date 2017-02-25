package de.mischok.konkretlive.registration.service.dtos;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Person {
	
	@NotBlank
	private String firstname;
	
	@NotBlank
	private String lastname;

	@NotBlank
	private String email;
	
	@NotNull
	private Date birthday;

	@NotBlank
	private String price;
	
	private boolean vegetarian;
	
	private boolean foodallergy;

	@NotBlank
	private String mobile;

	private String medicalhints;
	
	public Person() {}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the vegetarian
	 */
	public boolean isVegetarian() {
		return vegetarian;
	}

	/**
	 * @param vegetarian the vegetarian to set
	 */
	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}

	/**
	 * @return the foodallergy
	 */
	public boolean isFoodallergy() {
		return foodallergy;
	}

	/**
	 * @param foodallergy the foodallergy to set
	 */
	public void setFoodallergy(boolean foodallergy) {
		this.foodallergy = foodallergy;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the medicalhints
	 */
	public String getMedicalhints() {
		return medicalhints;
	}

	/**
	 * @param medicalhints the medicalhints to set
	 */
	public void setMedicalhints(String medicalhints) {
		this.medicalhints = medicalhints;
	}
}
