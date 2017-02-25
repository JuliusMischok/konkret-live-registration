package de.mischok.konkretlive.registration.service.dtos;

import java.util.List;

public class Registration {
	private Group group;
	private Leader leader;
	private List<Person> participants;
	
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
}
