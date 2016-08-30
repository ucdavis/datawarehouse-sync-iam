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

import edu.ucdavis.dss.datawarehouse.sync.iam.Version;

@Entity
@Table( name = "iam_pps_depts" )
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
	private Date vers;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getOrgOId() {
		return orgOId;
	}
	
	public void setOrgOId(String orgOId) {
		this.orgOId = orgOId;
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
	public boolean getUCDHS() {
		return ucdhs;
	}
	
	public void setUCDHS(boolean ucdhs) {
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
	
	@Column
	public Date getVers() {
		return vers;
	}
	public void setVers(Date vers) {
		this.vers = vers;
	}
	public void markAsVersion(Version vers) {
		this.vers = vers.getVers();
	}
	
	@Override
	public String toString() {
		return String.format(
				"IamDepartment[deptOfficialName='%s', deptDisplayName='%s', deptCode='%s']",
				deptOfficialName, deptDisplayName, deptCode);
	}
}
