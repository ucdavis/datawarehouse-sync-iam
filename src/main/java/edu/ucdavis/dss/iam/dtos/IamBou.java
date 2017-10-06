package edu.ucdavis.dss.iam.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table( name = "iam_bous" )
public class IamBou {
	private String orgOId, deptCode, deptOfficialName, deptDisplayName, deptAbbrev;
	private Boolean isUCDHS;
	private Date createDate, modifyDate;
	
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

	@Id
	@Column
	public String getOrgOId() { return orgOId; }
	public void setOrgOId(String orgOId) { this.orgOId = orgOId; }

	@Column
	public String getDeptCode() { return deptCode; }
	public void setDeptCode(String deptCode) { this.deptCode = deptCode; }

	@Column
	public String getDeptOfficialName() { return deptOfficialName; }
	public void setDeptOfficialName(String deptOfficialName) { this.deptOfficialName = deptOfficialName; }

	@Column
	public String getDeptDisplayName() { return deptDisplayName; }
	public void setDeptDisplayName(String deptDisplayName) { this.deptDisplayName = deptDisplayName; }

	@Column
	public String getDeptAbbrev() { return deptAbbrev; }
	public void setDeptAbbrev(String deptAbbrev) { this.deptAbbrev = deptAbbrev; }

	@Column(name="isUCDHS")
	@JsonProperty("isUCDHS")
	public Boolean getUCDHS() { return isUCDHS; }
	public void setUCDHS(Boolean UCDHS) { isUCDHS = UCDHS; }
}
