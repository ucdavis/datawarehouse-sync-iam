package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "iam_pps_depts" )
public class IamPpsDepartment {
	private String orgOId;
	private String deptCode;
	private String deptOfficialName;
	private String deptDisplayName;
	private String deptAbbrev;
	private Boolean ucdhs;
	private String bouOrgOId;
	private Date createDate, modifyDate;
	private Date createdAt, updatedAt;
	
	@Id
	@Column
	public String getDeptCode() {
		return deptCode;
	}
	
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column
	public String getOrgOId() {
		return orgOId;
	}
	
	public void setOrgOId(String orgOId) {
		this.orgOId = orgOId;
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
	public Boolean getUCDHS() {
		return ucdhs;
	}
	
	public void setUCDHS(Boolean ucdhs) {
		this.ucdhs = ucdhs;
	}
	
	@Column
	public String getBouOrgOId() {
		return bouOrgOId;
	}
	public void setBouOrgOId(String bouOrgOId) {
		this.bouOrgOId = bouOrgOId;
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

	@PreUpdate
	private void beforeUpdate() {
		this.updatedAt = new Date();
	}

	@PrePersist
	private void beforeCreation() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	@Override
	public String toString() {
		return String.format(
				"IamDepartment[deptOfficialName='%s', deptDisplayName='%s', deptCode='%s']",
				deptOfficialName, deptDisplayName, deptCode);
	}
}
