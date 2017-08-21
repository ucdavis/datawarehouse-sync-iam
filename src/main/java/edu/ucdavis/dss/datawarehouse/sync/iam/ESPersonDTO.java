package edu.ucdavis.dss.datawarehouse.sync.iam;

/**
 * DTO for ElasticSearch person
 * 
 * Used by GSON for JSON encoding.
 * 
 * @author christopherthielen
 *
 */
public class ESPersonDTO {
	private String iamId, dFirstName, dLastName, userId, email, dMiddleName, oFirstName, oMiddleName, oLastName, dFullName, oFullName;

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
}
