package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "iam_pps_associations" )
public class IamPpsAssociation {
	private Long id;
	private Long iamId;
	private String assocRank, titleCode, deptCode;
	private String deptOfficialName, deptDisplayName, deptAbbrev;
	private String adminDeptCode, adminDeptOfficialName, adminDeptDisplayName, adminDeptAbbrev;
	private String apptDeptCode, apptDeptOfficialName, apptDeptDisplayName, apptDeptAbbrev;
	private boolean isUCDHS;
	private String bouOrgOId, adminBouOrgOId, apptBouOrgOId;
	private Date assocStartDate, assocEndDate;
	private String titleOfficialName, titleDisplayName;
	private String positionTypeCode, positionType, percentFullTime;
	private String emplClass, emplClassDesc;
	private Date createDate, modifyDate;
	private Date createdAt, updatedAt, lastSeen;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public Long getIamId() {
		return this.iamId;
	}
	public void setIamId(Long iamId) {
		this.iamId = iamId;
	}

	@Column
	public String getAssocRank() {
		return this.assocRank;
	}
	public void setAssocRank(String assocRank) {
		this.assocRank = assocRank;
	}

	@Column
	public String getTitleCode() {
		return this.titleCode;
	}
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	@Column
	public String getDeptCode() {
		return this.deptCode;
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

	@Column
	public String getAdminDeptCode() { return adminDeptCode; }
	public void setAdminDeptCode(String adminDeptCode) { this.adminDeptCode = adminDeptCode; }

	@Column
	public String getAdminDeptOfficialName() { return adminDeptOfficialName; }
	public void setAdminDeptOfficialName(String adminDeptOfficialName) { this.adminDeptOfficialName = adminDeptOfficialName; }

	@Column
	public String getAdminDeptDisplayName() { return adminDeptDisplayName; }
	public void setAdminDeptDisplayName(String adminDeptDisplayName) { this.adminDeptDisplayName = adminDeptDisplayName; }

	@Column
	public String getAdminDeptAbbrev() { return adminDeptAbbrev; }
	public void setAdminDeptAbbrev(String adminDeptAbbrev) { this.adminDeptAbbrev = adminDeptAbbrev; }

	@Column
	public String getApptDeptCode() { return apptDeptCode; }
	public void setApptDeptCode(String apptDeptCode) { this.apptDeptCode = apptDeptCode; }

	@Column
	public String getApptDeptOfficialName() { return apptDeptOfficialName; }
	public void setApptDeptOfficialName(String apptDeptOfficialName) { this.apptDeptOfficialName = apptDeptOfficialName; }

	@Column
	public String getApptDeptDisplayName() { return apptDeptDisplayName; }
	public void setApptDeptDisplayName(String apptDeptDisplayName) { this.apptDeptDisplayName = apptDeptDisplayName; }

	@Column
	public String getApptDeptAbbrev() { return apptDeptAbbrev; }
	public void setApptDeptAbbrev(String apptDeptAbbrev) { this.apptDeptAbbrev = apptDeptAbbrev; }

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
	public String getAdminBouOrgOId() { return adminBouOrgOId; }
	public void setAdminBouOrgOId(String adminBouOrgOId) { this.adminBouOrgOId = adminBouOrgOId; }

	@Column
	public String getApptBouOrgOId() { return apptBouOrgOId; }
	public void setApptBouOrgOId(String apptBouOrgOId) { this.apptBouOrgOId = apptBouOrgOId; }

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
	public String getEmplClass() {
		return emplClass;
	}
	public void setEmplClass(String emplClass) {
		this.emplClass = emplClass;
	}

	@Column
	public String getEmplClassDesc() {
		return emplClassDesc;
	}
	public void setEmplClassDesc(String emplClassDesc) {
		this.emplClassDesc = emplClassDesc;
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
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		IamPpsAssociation other = (IamPpsAssociation)obj;

		if((assocRank == null) && (other.assocRank != null)) {
			return false;
		} else if (assocRank.equals(other.assocRank) == false) {
			return false;
		}

		if((iamId == null) && (other.iamId != null)) {
			return false;
		} else if ((iamId != null) && iamId.equals(other.iamId) == false) {
			return false;
		}

		if((titleCode == null) && (other.titleCode != null)) {
			return false;
		} else if ((titleCode != null) && titleCode.equals(other.titleCode) == false) {
			return false;
		}

		if((deptCode == null) && (other.deptCode != null)) {
			return false;
		} else if ((deptCode != null) && deptCode.equals(other.deptCode) == false) {
			return false;
		}

		if((adminDeptCode == null) && (other.adminDeptCode != null)) {
			return false;
		} else if ((adminDeptCode != null) && adminDeptCode.equals(other.adminDeptCode) == false) {
			return false;
		}

		if((apptDeptCode == null) && (other.apptDeptCode != null)) {
			return false;
		} else if ((apptDeptCode != null) && apptDeptCode.equals(other.apptDeptCode) == false) {
			return false;
		}

		if((modifyDate == null) && (other.modifyDate != null)) {
			return false;
		} else if ((modifyDate != null) && modifyDate.equals(other.modifyDate) == false) {
			return false;
		}

		if((bouOrgOId == null) && (other.bouOrgOId != null)) {
			return false;
		} else if ((bouOrgOId != null) && bouOrgOId.equals(other.bouOrgOId) == false) {
			return false;
		}

		if((adminBouOrgOId == null) && (other.adminBouOrgOId != null)) {
			return false;
		} else if ((adminBouOrgOId != null) && adminBouOrgOId.equals(other.adminBouOrgOId) == false) {
			return false;
		}

		if((apptBouOrgOId == null) && (other.apptBouOrgOId != null)) {
			return false;
		} else if ((apptBouOrgOId != null) && apptBouOrgOId.equals(other.apptBouOrgOId) == false) {
			return false;
		}

		if((emplClass == null) && (other.emplClass != null)) {
			return false;
		} else if ((emplClass != null) && emplClass.equals(other.emplClass) == false) {
			return false;
		}

		if((emplClassDesc == null) && (other.emplClassDesc != null)) {
			return false;
		} else if ((emplClassDesc != null) && emplClassDesc.equals(other.emplClassDesc) == false) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((assocRank == null) ? 0 : assocRank.hashCode());
		result = prime * result + ((iamId == null) ? 0 : iamId.hashCode());
		result = prime * result + ((titleCode == null) ? 0 : titleCode.hashCode());
		result = prime * result + ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((adminDeptCode == null) ? 0 : adminDeptCode.hashCode());
		result = prime * result + ((apptDeptCode == null) ? 0 : apptDeptCode.hashCode());
		result = prime * result + ((modifyDate == null) ? 0 : modifyDate.hashCode());

		return result;
	}

	@Override
	public String toString() {
		return String.format(
				"IamPpsAssociation[iamId='%s',assocRank='%s',titleCode='%s',deptCode='%s']", iamId, assocRank, titleCode, deptCode);
	}
}
