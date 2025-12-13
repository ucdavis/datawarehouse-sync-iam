package edu.ucdavis.dss.iam.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table( name = "iam_sis_associations" )
public class IamSisAssociation {
	private Long id;
	private Long iamId;
	private String assocRank, levelCode, classCode, collegeCode, majorCode;
	private String levelName, className, collegeName;
	private Date assocStartDate, assocEndDate;
	private String majorName, fepraCode;
	private Date createDate, modifyDate;
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
	public String getAssocRank() { return this.assocRank; }
	public void setAssocRank(String assocRank) { this.assocRank = assocRank; }

	@Column
	public String getLevelCode() { return levelCode; }
	public void setLevelCode(String levelCode) { this.levelCode = levelCode; }

	@Column
	public String getClassCode() { return classCode; }
	public void setClassCode(String classCode) { this.classCode = classCode; }

	@Column
	public String getCollegeCode() { return collegeCode; }
	public void setCollegeCode(String collegeCode) { this.collegeCode = collegeCode; }

	@Column
	public String getMajorCode() { return majorCode; }
	public void setMajorCode(String majorCode) { this.majorCode = majorCode; }

	@Column
	public String getLevelName() { return levelName; }
	public void setLevelName(String levelName) { this.levelName = levelName; }

	@Column
	public String getClassName() { return className; }
	public void setClassName(String className) { this.className = className; }

	@Column
	public String getCollegeName() { return collegeName; }
	public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

	@Column
	public String getMajorName() { return majorName; }
	public void setMajorName(String majorName) { this.majorName = majorName; }

	@Column
	public String getFepraCode() { return fepraCode; }
	public void setFepraCode(String fepraCode) { this.fepraCode = fepraCode; }

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yy")
	public Date getAssocStartDate() {
		return assocStartDate;
	}
	public void setAssocStartDate(Date assocStartDate) {
		this.assocStartDate = assocStartDate;
	}

	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yy")
	public Date getAssocEndDate() {
		return assocEndDate;
	}
	public void setAssocEndDate(Date assocEndDate) {
		this.assocEndDate = assocEndDate;
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
	private void beforeUpdate() { this.updatedAt = new Date(); }

	@PrePersist
	private void beforeCreation() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	public Date getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		IamSisAssociation other = (IamSisAssociation)obj;

		if((assocRank == null) && (other.assocRank != null)) {
			return false;
		} else if((assocRank != null) && assocRank.equals(other.assocRank) == false) {
			return false;
		}

		if((iamId == null) && (other.iamId != null)) {
			return false;
		} else if((iamId != null) && iamId.equals(other.iamId) == false) {
			return false;
		}

		if((levelCode == null) && (other.levelCode != null)) {
			return false;
		} else if((levelCode != null) && levelCode.equals(other.levelCode) == false) {
			return false;
		}

		if((classCode == null) && (other.classCode != null)) {
			return false;
		} else if((classCode != null) && classCode.equals(other.classCode) == false) {
			return false;
		}

		if((collegeCode == null) && (other.collegeCode != null)) {
			return false;
		} else if((collegeCode != null) && collegeCode.equals(other.collegeCode) == false) {
			return false;
		}

		if((majorCode == null) && (other.majorCode != null)) {
			return false;
		} else if((majorCode != null) && majorCode.equals(other.majorCode) == false) {
			return false;
		}

		if((modifyDate == null) && (other.modifyDate != null)) {
			return false;
		} else if((modifyDate != null) && modifyDate.equals(other.modifyDate) == false) {
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
		result = prime * result + ((modifyDate == null) ? 0 : modifyDate.hashCode());

		return result;
	}

	@Override
	public String toString() {
		return String.format(
				"IamSisAssociation[iamId='%s', majorCode='%s', collegeCode='%s']",
				iamId, majorCode, collegeCode);
	}
}
