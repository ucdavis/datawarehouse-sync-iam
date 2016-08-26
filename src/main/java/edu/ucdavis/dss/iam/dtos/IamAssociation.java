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
@Table( name = "iam_associations" )
public class IamAssociation {
	private Long id;
	private Long iamId;
	private String deptCode, deptOfficialName, deptDisplayName, deptAbbrev;
	private boolean isUCDHS;
	private String bouOrgOId;
	private String assocRank;
	private Date assocStartDate, assocEndDate;
	private String titleCode, titleOfficialName, titleDisplayName;
	private String positionTypeCode, positionType, percentFullTime;
	private Date createDate, modifyDate;
	
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	@Column
	public String getDeptOfficialName() {
		return deptOfficialName;
	}
	public void setDeptOfficialName(String deptOfficialName) {
		this.deptOfficialName = deptOfficialName;
	}
	
	@Column
	public String getDeptDisplayName() {
		return deptDisplayName;
	}
	public void setDeptDisplayName(String deptDisplayName) {
		this.deptDisplayName = deptDisplayName;
	}
	
	@Column
	public String getDeptAbbrev() {
		return deptAbbrev;
	}
	public void setDeptAbbrev(String deptAbbrev) {
		this.deptAbbrev = deptAbbrev;
	}
	
	@Column(name="isUCDHS")
	@JsonProperty("isUCDHS")
	public boolean isUCDHS() {
		return isUCDHS;
	}
	public void setUCDHS(boolean isUCDHS) {
		this.isUCDHS = isUCDHS;
	}
	
	@Column
	public String getBouOrgOId() {
		return bouOrgOId;
	}
	public void setBouOrgOId(String bouOrgOId) {
		this.bouOrgOId = bouOrgOId;
	}
	
	@Column
	public String getAssocRank() {
		return assocRank;
	}
	public void setAssocRank(String assocRank) {
		this.assocRank = assocRank;
	}
	
	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	public Date getAssocStartDate() {
		return assocStartDate;
	}
	public void setAssocStartDate(Date assocStartDate) {
		this.assocStartDate = assocStartDate;
	}
	
	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	public Date getAssocEndDate() {
		return assocEndDate;
	}
	public void setAssocEndDate(Date assocEndDate) {
		this.assocEndDate = assocEndDate;
	}
	
	@Column
	public String getTitleCode() {
		return titleCode;
	}
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	
	@Column
	public String getTitleOfficialName() {
		return titleOfficialName;
	}
	public void setTitleOfficialName(String titleOfficialName) {
		this.titleOfficialName = titleOfficialName;
	}
	
	@Column
	public String getTitleDisplayName() {
		return titleDisplayName;
	}
	public void setTitleDisplayName(String titleDisplayName) {
		this.titleDisplayName = titleDisplayName;
	}
	
	@Column
	public String getPositionTypeCode() {
		return positionTypeCode;
	}
	public void setPositionTypeCode(String positionTypeCode) {
		this.positionTypeCode = positionTypeCode;
	}
	
	@Column
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	
	@Column
	public String getPercentFullTime() {
		return percentFullTime;
	}
	public void setPercentFullTime(String percentFullTime) {
		this.percentFullTime = percentFullTime;
	}
	
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column
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
