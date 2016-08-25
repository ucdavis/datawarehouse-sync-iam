package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IamDepartment {
	private Long id;
	private String orgOId;
	private String deptCode;
	private String deptOfficialName;
	private String deptDisplayName;
	private String deptAbbrev;
	private boolean ucdhs;
	private String bouOrgOId;
	private Date createDate, modifyDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOrgOId() {
		return orgOId;
	}
	
	public void setOrgOId(String orgOId) {
		this.orgOId = orgOId;
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
	public boolean getUCDHS() {
		return ucdhs;
	}
	
	public void setUCDHS(boolean ucdhs) {
		this.ucdhs = ucdhs;
	}
	
	public String getBouOrgOId() {
		return bouOrgOId;
	}
	
	public void setBouOrgOId(String bouOrgOId) {
		this.bouOrgOId = bouOrgOId;
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
				"IamDepartment[deptOfficialName='%s', deptDisplayName='%s', deptCode='%s']",
				deptOfficialName, deptDisplayName, deptCode);
	}
}
