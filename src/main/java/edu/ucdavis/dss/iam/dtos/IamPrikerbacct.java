package edu.ucdavis.dss.iam.dtos;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table( name = "iam_prikerbacct" )
public class IamPrikerbacct {
	private Long id;
	private Long iamId;
	private String userId, uuId;
	private Date createDate, claimDate, expireDate;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column
	public String getUuId() {
		return uuId;
	}
	public void setUuId(String uuId) {
		this.uuId = uuId;
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
	public Date getClaimDate() {
		return claimDate;
	}
	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate;
	}
	
	@Column
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((iamId == null) ? 0 : iamId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((uuId == null) ? 0 : uuId.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((claimDate == null) ? 0 : claimDate.hashCode());
		result = prime * result + ((expireDate == null) ? 0 : expireDate.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		IamPrikerbacct other = (IamPrikerbacct) obj;

		if((iamId == null) && (other.iamId != null)) {
			return false;
		} else if((iamId != null) && iamId.equals(other.iamId) == false) {
			return false;
		}

		if((userId == null) && (other.userId != null)) {
			return false;
		} else if((userId != null) && userId.equals(other.userId) == false) {
			return false;
		}

		if((uuId == null) && (other.uuId != null)) {
			return false;
		} else if((uuId != null) && uuId.equals(other.uuId) == false) {
			return false;
		}

		if((createDate == null) && (other.createDate != null)) {
			return false;
		} else if((createDate != null) && createDate.equals(other.createDate) == false) {
			return false;
		}

		if((claimDate == null) && (other.claimDate != null)) {
			return false;
		} else if((claimDate != null) && claimDate.equals(other.claimDate) == false) {
			return false;
		}

		if((expireDate == null) && (other.expireDate != null)) {
			return false;
		} else if((expireDate != null) && expireDate.equals(other.expireDate) == false) {
			return false;
		}

		return true;
	}
}
