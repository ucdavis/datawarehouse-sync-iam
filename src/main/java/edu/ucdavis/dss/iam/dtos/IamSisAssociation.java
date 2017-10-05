package edu.ucdavis.dss.iam.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table( name = "iam_sis_associations" )
public class IamSisAssociation {
	private String levelCode, levelName, classCode, className, collegeCode, collegeName;
	private Date assocStartDate, assocEndDate;
	private String majorCode, majorName, fepraCode;
	private Date createDate, modifyDate;

	@EmbeddedId
	private IamSisAssociationPK associationPK = new IamSisAssociationPK();
	
	public IamSisAssociationPK getId() {
		return associationPK;
	}
	public void setId(IamSisAssociationPK associationPK) {
		this.associationPK = associationPK;
	}

	public Long getIamId() {
		return associationPK.getIamId();
	}
	public void setIamId(Long iamId) {
		this.associationPK.setIamId(iamId);
	}

	@Column
	public String getLevelCode() { return levelCode; }
	public void setLevelCode(String levelCode) { this.levelCode = levelCode; }

	@Column
	public String getLevelName() { return levelName; }
	public void setLevelName(String levelName) { this.levelName = levelName; }

	@Column
	public String getClassCode() { return classCode; }
	public void setClassCode(String classCode) { this.classCode = classCode; }

	@Column
	public String getClassName() { return className; }
	public void setClassName(String className) { this.className = className; }

	@Column
	public String getCollegeCode() { return collegeCode; }
	public void setCollegeCode(String collegeCode) { this.collegeCode = collegeCode; }

	@Column
	public String getCollegeName() { return collegeName; }
	public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

	@Column
	public String getMajorCode() { return majorCode; }
	public void setMajorCode(String majorCode) { this.majorCode = majorCode; }

	@Column
	public String getMajorName() { return majorName; }
	public void setMajorName(String majorName) { this.majorName = majorName; }

	@Column
	public String getFepraCode() { return fepraCode; }
	public void setFepraCode(String fepraCode) { this.fepraCode = fepraCode; }

	public String getAssocRank() {
		return associationPK.getAssocRank();
	}
	public void setAssocRank(String assocRank) {
		this.associationPK.setAssocRank(assocRank);
	}

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

	@Override
	public String toString() {
		return String.format(
				"IamSisAssociation[PK='%s', majorCode='%s', collegeCode='%s']",
				associationPK, majorCode, collegeCode);
	}
}
