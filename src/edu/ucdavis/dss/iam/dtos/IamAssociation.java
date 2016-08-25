package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IamAssociation {
	private String iamId;
	private String deptCode, deptOfficialName, deptDisplayName, deptAbbrev;
	private boolean isUCDHS;
	private String bouOrgOId;
	private String assocRank, assocStartDate, assocEndDate;
	private String titleCode, titleOfficialName, titleDisplayName;
	private String positionTypeCode, positionType, percentFullTime;
	private Date createDate, modifyDate;
	
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
	public boolean isUCDHS() {
		return isUCDHS;
	}
	public void setUCDHS(boolean isUCDHS) {
		this.isUCDHS = isUCDHS;
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
	
	public String getAssocStartDate() {
		return assocStartDate;
	}
	public void setAssocStartDate(String assocStartDate) {
		this.assocStartDate = assocStartDate;
	}
	
	public String getAssocEndDate() {
		return assocEndDate;
	}
	public void setAssocEndDate(String assocEndDate) {
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
	
	@Override
	public String toString() {
		return String.format(
				"IamAssociation[iamId='%s', deptCode='%s', titleCode='%s']",
				iamId, deptCode, titleCode);
	}
}
