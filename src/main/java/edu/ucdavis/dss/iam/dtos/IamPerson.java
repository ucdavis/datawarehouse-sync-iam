package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	public boolean getEmployeeInd() {
		return employeeInd;
	}
	
	public void setEmployeeInd(boolean employeeInd) {
		this.employeeInd = employeeInd;
	}
	
	@Column(name="isHSEmployee")
	@JsonProperty("isHSEmployee")
	public boolean getHsEmployeeInd() {
		return hsEmployeeInd;
	}
	
	public void sethsEmployeeInd(boolean hsEmployeeInd) {
		this.hsEmployeeInd = hsEmployeeInd;
	}
	
	@Column(name="isFaculty")
	@JsonProperty("isFaculty")
	public boolean getFacultyInd() {
		return facultyInd;
	}
	
	public void setFacultyInd(boolean facultyInd) {
		this.facultyInd = facultyInd;
	}
	
	@Column(name="isStudent")
	@JsonProperty("isStudent")
	public boolean getStudentInd() {
		return studentInd;
	}
	
	public void setStudentInd(boolean studentInd) {
		this.studentInd = studentInd;
	}
	
	@Column(name="isStaff")
	@JsonProperty("isStaff")
	public boolean getStaffInd() {
		return staffInd;
	}
	
	public void setStaffInd(boolean staffInd) {
		this.staffInd = staffInd;
	}
	
	@Column(name="isExternal")
	@JsonProperty("isExternal")
	public boolean getExternalInd() {
		return externalInd;
	}
	
	public void setExternalInd(boolean externalInd) {
		this.externalInd = externalInd;
	}
	
	@Column
	public String getPrivacyCode() {
		return privacyCode;
	}
	
	public void setPrivacyCode(String privacyCode) {
		this.privacyCode = privacyCode;
	}
	
	@Override
	public String toString() {
		return String.format(
				"IamPerson[iamId='%s', ppsId='%s', dFullName='%s', oFullName='%s']",
				iamId, ppsId, dFullName, oFullName);
	}
}
