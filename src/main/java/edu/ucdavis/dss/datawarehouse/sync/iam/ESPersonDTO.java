package edu.ucdavis.dss.datawarehouse.sync.iam;

import edu.ucdavis.dss.iam.dtos.*;

import java.util.List;
import java.util.Date;

/**
 * DTO for ElasticSearch person
 * 
 * Used by GSON for JSON encoding.
 * 
 * @author christopherthielen
 *
 */
public class ESPersonDTO {
	private String iamId;
	private String dFirstName, dLastName, userId, email, dMiddleName, oFirstName, oMiddleName, oLastName, dFullName, oFullName;
	private List<IamContactInfo> contactInfos;
	private List<IamPerson> people;
	private List<IamPrikerbacct> prikerbaccts;
	private List<IamPpsAssociation> ppsAssociations;
	private List<IamSisAssociation> sisAssociations;
	private Date lastSeen;

	public String getIamId() {
		return iamId;
	}

	public void setIamId(String iamId) {
		this.iamId = iamId;
	}

	public String getdFirstName() {
		return dFirstName;
	}

	public void setdFirstName(String dFirstName) {
		this.dFirstName = dFirstName;
	}

	public String getdLastName() {
		return dLastName;
	}

	public void setdLastName(String dLastName) {
		this.dLastName = dLastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getdMiddleName() {
		return dMiddleName;
	}

	public void setdMiddleName(String dMiddleName) {
		this.dMiddleName = dMiddleName;
	}

	public String getoFirstName() {
		return oFirstName;
	}

	public void setoFirstName(String oFirstName) {
		this.oFirstName = oFirstName;
	}

	public String getoMiddleName() {
		return oMiddleName;
	}

	public void setoMiddleName(String oMiddleName) {
		this.oMiddleName = oMiddleName;
	}

	public String getoLastName() {
		return oLastName;
	}

	public void setoLastName(String oLastName) {
		this.oLastName = oLastName;
	}

	public String getdFullName() {
		return dFullName;
	}

	public void setdFullName(String dFullName) {
		this.dFullName = dFullName;
	}

	public String getoFullName() {
		return oFullName;
	}

	public void setoFullName(String oFullName) {
		this.oFullName = oFullName;
	}

	public List<IamContactInfo> getContactInfos() {
		return contactInfos;
	}

	public void setContactInfos(List<IamContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
	}

	public List<IamPerson> getPeople() {
		return people;
	}

	public void setPeople(List<IamPerson> people) {
		this.people = people;
	}

	public List<IamPrikerbacct> getPrikerbaccts() {
		return prikerbaccts;
	}

	public void setPrikerbaccts(List<IamPrikerbacct> prikerbaccts) {
		this.prikerbaccts = prikerbaccts;
	}

	public List<IamPpsAssociation> getPpsAssociations() {
		return ppsAssociations;
	}

	public void setPpsAssociations(List<IamPpsAssociation> ppsAssociations) {
		this.ppsAssociations = ppsAssociations;
	}

	public List<IamSisAssociation> getSisAssociations() {
		return sisAssociations;
	}

	public void setSisAssociations(List<IamSisAssociation> sisAssociations) {
		this.sisAssociations = sisAssociations;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}
}
