package de.mischok.konkretlive.registration.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Group {
	
	@Id
	private String id;
	
	@NotBlank
	private String church;
	
	@NotBlank
	private String type;
	
	@NotBlank
	private String district;
	
	public Group() {}

	/**
	 * @return the church
	 */
	public String getChurch() {
		return church;
	}

	/**
	 * @param church the church to set
	 */
	public void setChurch(String church) {
		this.church = church;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
