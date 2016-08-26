package edu.ucdavis.dss.iam.dtos;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IamPerson {
	private Long id;
	private Long iamId;
	private String mothraId;
	private String ppsId;
	private String studentId;
	private String bannerPIdM;
	private String externalId;
	private String oFirstName, oMiddleName, oLastName, oFullName, oSuffix;
	private String dFirstName, dMiddleName, dLastName, dSuffix, dFullName;
	private Boolean isEmployee, isHSEmployee ,isFaculty ,isStudent, isStaff, isExternal;
	private String privacyCode;
	private Date modifyDate;
	
	private Set<IamAssociation> associations;
	private Set<IamContactInfo> contactInfos;
	private Set<IamPrikerbacct> prikerbaccts;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIamId() {
		return iamId;
	}
	
	public void setIamId(Long iamId) {
		this.iamId = iamId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getModifyDate() {
		return modifyDate;
	}
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public String getMothraId() {
		return mothraId;
	}
	
	public void setMothraId(String mothraId) {
		this.mothraId = mothraId;
	}
	
	public String getPpsId() {
		return ppsId;
	}
	
	public void setPpsId(String ppsId) {
		this.ppsId = ppsId;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getBannerPIdM() {
		return bannerPIdM;
	}
	
	public void setBannerPIdM(String bannerPIdM) {
		this.bannerPIdM = bannerPIdM;
	}
	
	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
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

	public String getoFullName() {
		return oFullName;
	}
	
	public void setoFullName(String oFullName) {
		this.oFullName = oFullName;
	}
	
	public String getoSuffix() {
		return oSuffix;
	}
	
	public void setoSuffix(String oSuffix) {
		this.oSuffix = oSuffix;
	}
	
	public String getdFirstName() {
		return dFirstName;
	}
	
	public void setdFirstName(String dFirstName) {
		this.dFirstName = dFirstName;
	}
	
	public String getdMiddleName() {
		return dMiddleName;
	}
	
	public void setdMiddleName(String dMiddleName) {
		this.dMiddleName = dMiddleName;
	}
	
	public String getdLastName() {
		return dLastName;
	}
	
	public void setdLastName(String dLastName) {
		this.dLastName = dLastName;
	}
	
	public String getdSuffix() {
		return dSuffix;
	}
	
	public void setdSuffix(String dSuffix) {
		this.dSuffix = dSuffix;
	}
	
	public String getdFullName() {
		return dFullName;
	}
	
	public void setdFullName(String dFullName) {
		this.dFullName = dFullName;
	}
	
	@JsonProperty("isEmployee")
	public boolean isEmployee() {
		return isEmployee;
	}
	
	public void setIsEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}
	
	@JsonProperty("isHSEmployee")
	public boolean isHSEmployee() {
		return isHSEmployee;
	}
	
	public void setIsHSEmployee(boolean isHSEmployee) {
		this.isHSEmployee = isHSEmployee;
	}
	
	@JsonProperty("isFaculty")
	public boolean isFaculty() {
		return isFaculty;
	}
	
	public void setIsFaculty(boolean isFaculty) {
		this.isFaculty = isFaculty;
	}
	
	@JsonProperty("isStudent")
	public boolean isStudent() {
		return isStudent;
	}
	
	public void setIsStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
	
	@JsonProperty("isStaff")
	public boolean isStaff() {
		return isStaff;
	}
	
	public void setIsStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}
	
	@JsonProperty("isExternal")
	public boolean isExternal() {
		return isExternal;
	}
	
	public void setIsExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}
	
	public String getPrivacyCode() {
		return privacyCode;
	}
	
	public void setPrivacyCode(String privacyCode) {
		this.privacyCode = privacyCode;
	}
	
	public Set<IamAssociation> getAssociations() {
		return associations;
	}

	public void setAssociations(Set<IamAssociation> associations) {
		this.associations = associations;
	}

	public Set<IamContactInfo> getContactInfos() {
		return contactInfos;
	}
	public void setContactInfos(Set<IamContactInfo> contactInfos) {
		this.contactInfos = contactInfos;
	}
	public Set<IamPrikerbacct> getPrikerbaccts() {
		return prikerbaccts;
	}
	public void setPrikerbaccts(Set<IamPrikerbacct> prikerbaccts) {
		this.prikerbaccts = prikerbaccts;
	}

	@Override
	public String toString() {
		return String.format(
				"IamPerson[iamId='%s', ppsId='%s', dFullName='%s']",
				iamId, ppsId, dFullName);
	}
}
