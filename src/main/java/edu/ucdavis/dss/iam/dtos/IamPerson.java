package edu.ucdavis.dss.iam.dtos;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IamPerson {
	private String iamId;
	private String deptCode;
	private String deptOfficialName;
	private String deptDisplayName;
	private String deptAbbrev;
	private String loginId;
	private boolean ucdhs;
	private String bouOrgOId;
	private String assocRank;
	private Date assocStartDate, assocEndDate;
	private String titleCode;
	private String titleOfficialName;
	private String titleDisplayName;
	private String positionTypeCode, positionType;
	private String percentFullTime;
	private Date createDate, modifyDate;
	private String mothraId;
	private String ppsId;
	private String studentId;
	private String bannerPIdM;
	private String externalId;
	private String oFirstName, oMiddleName, oLastName, oSuffix;
	private String dFirstName, dMiddleName, dLastName, dSuffix;
	private String oFullName, dFullName;
	private boolean isEmployee, isHSEmployee, isFaculty, isStudent, isStaff, isExternal;
	private String privacyCode;
	private String email, hsEmail, campusEmail;
	private String addrStreet, addrCity, addrState, addrZip, postalAddress;
	private String workPhone, workCell, workPager, workFax;
	private List<IamAssociation> associations;
	
	public String getIamId() {
		return iamId;
	}
	
	public void setIamId(String iamId) {
		this.iamId = iamId;
	}
	
	public String getDeptCode() {
		return deptCode;
	}
	
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	public String getDeptOfficialName() {
		return deptOfficialName;
	}
	
	public void setDeptOfficialName(String deptOfficialName) {
		this.deptOfficialName = deptOfficialName;
	}
	
	public String getDeptDisplayName() {
		return deptDisplayName;
	}
	
	public void setDeptDisplayName(String deptDisplayName) {
		this.deptDisplayName = deptDisplayName;
	}
	
	public String getDeptAbbrev() {
		return deptAbbrev;
	}
	
	public void setDeptAbbrev(String deptAbbrev) {
		this.deptAbbrev = deptAbbrev;
	}
	
	@JsonProperty("isUCDHS")
	public boolean isUcdhs() {
		return ucdhs;
	}
	
	public void setUcdhs(boolean ucdhs) {
		this.ucdhs = ucdhs;
	}
	
	public String getBouOrgOId() {
		return bouOrgOId;
	}
	
	public void setBouOrgOId(String bouOrgOId) {
		this.bouOrgOId = bouOrgOId;
	}
	
	public String getAssocRank() {
		return assocRank;
	}
	
	public void setAssocRank(String assocRank) {
		this.assocRank = assocRank;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAssocStartDate() {
		return assocStartDate;
	}
	
	public void setAssocStartDate(Date assocStartDate) {
		this.assocStartDate = assocStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAssocEndDate() {
		return assocEndDate;
	}
	
	public void setAssocEndDate(Date assocEndDate) {
		this.assocEndDate = assocEndDate;
	}
	
	public String getTitleCode() {
		return titleCode;
	}
	
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	
	public String getTitleOfficialName() {
		return titleOfficialName;
	}
	
	public void setTitleOfficialName(String titleOfficialName) {
		this.titleOfficialName = titleOfficialName;
	}
	
	public String getTitleDisplayName() {
		return titleDisplayName;
	}
	
	public void setTitleDisplayName(String titleDisplayName) {
		this.titleDisplayName = titleDisplayName;
	}
	
	public String getPositionTypeCode() {
		return positionTypeCode;
	}
	
	public void setPositionTypeCode(String positionTypeCode) {
		this.positionTypeCode = positionTypeCode;
	}
	
	public String getPositionType() {
		return positionType;
	}
	
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	
	public String getPercentFullTime() {
		return percentFullTime;
	}
	
	public void setPercentFullTime(String percentFullTime) {
		this.percentFullTime = percentFullTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getHsEmail() {
		return hsEmail;
	}
	
	public void setHsEmail(String hsEmail) {
		this.hsEmail = hsEmail;
	}
	
	public String getCampusEmail() {
		return campusEmail;
	}
	
	public void setCampusEmail(String campusEmail) {
		this.campusEmail = campusEmail;
	}
	
	public String getAddrStreet() {
		return addrStreet;
	}
	
	public void setAddrStreet(String addrStreet) {
		this.addrStreet = addrStreet;
	}
	
	public String getAddrCity() {
		return addrCity;
	}
	
	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}
	
	public String getAddrState() {
		return addrState;
	}
	
	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}
	
	public String getAddrZip() {
		return addrZip;
	}
	
	public void setAddrZip(String addrZip) {
		this.addrZip = addrZip;
	}
	
	public String getPostalAddress() {
		return postalAddress;
	}
	
	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}
	
	public String getWorkPhone() {
		return workPhone;
	}
	
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	
	public String getWorkCell() {
		return workCell;
	}
	
	public void setWorkCell(String workCell) {
		this.workCell = workCell;
	}
	
	public String getWorkPager() {
		return workPager;
	}
	
	public void setWorkPager(String workPager) {
		this.workPager = workPager;
	}
	
	public String getWorkFax() {
		return workFax;
	}
	
	public void setWorkFax(String workFax) {
		this.workFax = workFax;
	}
	
	public String getLoginId() {
		return loginId;
	}
	
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public List<IamAssociation> getAssociations() {
		return associations;
	}

	public void setAssociations(List<IamAssociation> associations) {
		this.associations = associations;
	}

	@Override
	public String toString() {
		return String.format(
				"IamPerson[iamId='%s', deptCode='%s', titleCode='%s', ppsId='%s', loginId='%s']",
				iamId, deptCode, titleCode, ppsId, loginId);
	}
}
