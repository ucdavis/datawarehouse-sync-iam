package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "iam_people" )
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
	private Boolean employeeInd, hsEmployeeInd, facultyInd, studentInd, staffInd, externalInd;
	private String privacyCode;
	private Date modifyDate;
	private Date createdAt, updatedAt, lastSeen;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public Long getIamId() {
		return iamId;
	}
	public void setIamId(Long iamId) {
		this.iamId = iamId;
	}
	
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getModifyDate() {
		return modifyDate;
	}
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@Column
	public String getMothraId() {
		return mothraId;
	}
	
	public void setMothraId(String mothraId) {
		this.mothraId = mothraId;
	}
	
	@Column
	public String getPpsId() {
		return ppsId;
	}
	
	public void setPpsId(String ppsId) {
		this.ppsId = ppsId;
	}
	
	@Column
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	@Column
	public String getBannerPIdM() {
		return bannerPIdM;
	}
	
	public void setBannerPIdM(String bannerPIdM) {
		this.bannerPIdM = bannerPIdM;
	}
	
	@Column
	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	@Column
	public String getoFirstName() {
		return oFirstName;
	}
	
	public void setoFirstName(String oFirstName) {
		this.oFirstName = oFirstName;
	}
	
	@Column
	public String getoMiddleName() {
		return oMiddleName;
	}
	
	public void setoMiddleName(String oMiddleName) {
		this.oMiddleName = oMiddleName;
	}

	@Column
	public String getoLastName() {
		return oLastName;
	}
	
	public void setoLastName(String oLastName) {
		this.oLastName = oLastName;
	}

	@Column
	public String getoFullName() {
		return oFullName;
	}
	
	public void setoFullName(String oFullName) {
		this.oFullName = oFullName;
	}
	
	@Column
	public String getoSuffix() {
		return oSuffix;
	}
	
	public void setoSuffix(String oSuffix) {
		this.oSuffix = oSuffix;
	}
	
	@Column
	public String getdFirstName() {
		return dFirstName;
	}
	
	public void setdFirstName(String dFirstName) {
		this.dFirstName = dFirstName;
	}
	
	@Column
	public String getdMiddleName() {
		return dMiddleName;
	}
	
	public void setdMiddleName(String dMiddleName) {
		this.dMiddleName = dMiddleName;
	}
	
	@Column
	public String getdLastName() {
		return dLastName;
	}
	
	public void setdLastName(String dLastName) {
		this.dLastName = dLastName;
	}
	
	@Column
	public String getdSuffix() {
		return dSuffix;
	}
	
	public void setdSuffix(String dSuffix) {
		this.dSuffix = dSuffix;
	}
	
	@Column
	public String getdFullName() {
		return dFullName;
	}
	
	public void setdFullName(String dFullName) {
		this.dFullName = dFullName;
	}
	
	@Column(name="isEmployee")
	@JsonProperty("isEmployee")
	public Boolean getEmployeeInd() {
		return employeeInd;
	}
	
	public void setEmployeeInd(Boolean employeeInd) {
		this.employeeInd = employeeInd;
	}
	
	@Column(name="isHSEmployee")
	@JsonProperty("isHSEmployee")
	public Boolean getHsEmployeeInd() {
		return hsEmployeeInd;
	}
	
	public void sethsEmployeeInd(Boolean hsEmployeeInd) {
		this.hsEmployeeInd = hsEmployeeInd;
	}
	
	@Column(name="isFaculty")
	@JsonProperty("isFaculty")
	public Boolean getFacultyInd() {
		return facultyInd;
	}
	
	public void setFacultyInd(Boolean facultyInd) {
		this.facultyInd = facultyInd;
	}
	
	@Column(name="isStudent")
	@JsonProperty("isStudent")
	public Boolean getStudentInd() {
		return studentInd;
	}
	
	public void setStudentInd(Boolean studentInd) {
		this.studentInd = studentInd;
	}
	
	@Column(name="isStaff")
	@JsonProperty("isStaff")
	public Boolean getStaffInd() {
		return staffInd;
	}
	public void setStaffInd(Boolean staffInd) {
		this.staffInd = staffInd;
	}
	
	@Column(name="isExternal")
	@JsonProperty("isExternal")
	public Boolean getExternalInd() {
		return externalInd;
	}
	public void setExternalInd(Boolean externalInd) {
		this.externalInd = externalInd;
	}
	
	@Column
	public String getPrivacyCode() {
		return privacyCode;
	}
	public void setPrivacyCode(String privacyCode) {
		this.privacyCode = privacyCode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	@PreUpdate
	private void beforeUpdate() {
		this.updatedAt = new Date();
	}

	@PrePersist
	private void beforeCreation() {
		this.createdAt = this.updatedAt = new Date();
	}

	@Override
	public String toString() {
		return String.format(
				"IamPerson[iamId='%s', ppsId='%s', dFullName='%s', oFullName='%s']",
				iamId, ppsId, dFullName, oFullName);
	}
}
