package de.mischok.konkretlive.registration.model;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Registration {
	
	@Id
	private String id;
	
	@DBRef
	@Valid
	private Group group;
	
	@DBRef
	@Valid
	private Leader leader;
	
	@DBRef
	@Valid
	private List<Person> participants;
	
	public Registration() {}
	
	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}
	
	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}
	
	/**
	 * @return the leader
	 */
	public Leader getLeader() {
		return leader;
	}
	
	/**
	 * @param leader the leader to set
	 */
	public void setLeader(Leader leader) {
		this.leader = leader;
	}
	
	/**
	 * @return the participants
	 */
	public List<Person> getParticipants() {
		return participants;
	}
	
	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<Person> participants) {
		this.participants = participants;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
