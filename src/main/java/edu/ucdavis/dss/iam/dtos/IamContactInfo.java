package edu.ucdavis.dss.iam.dtos;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "iam_contactinfo" )
public class IamContactInfo {
	private Long id;
	private Long iamId;
	private String email, hsEmail, campusEmail;
	private String addrStreet, addrCity, addrState, addrZip, postalAddress;
	private String workPhone, workCell, workPager, workFax;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column
	public String getHsEmail() {
		return hsEmail;
	}
	public void setHsEmail(String hsEmail) {
		this.hsEmail = hsEmail;
	}
	
	@Column
	public String getCampusEmail() {
		return campusEmail;
	}
	public void setCampusEmail(String campusEmail) {
		this.campusEmail = campusEmail;
	}
	
	@Column
	public String getAddrStreet() {
		return addrStreet;
	}
	public void setAddrStreet(String addrStreet) {
		this.addrStreet = addrStreet;
	}
	
	@Column
	public String getAddrCity() {
		return addrCity;
	}
	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}
	
	@Column
	public String getAddrState() {
		return addrState;
	}
	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}
	
	@Column
	public String getAddrZip() {
		return addrZip;
	}
	public void setAddrZip(String addrZip) {
		this.addrZip = addrZip;
	}
	
	@Column
	public String getPostalAddress() {
		return postalAddress;
	}
	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}
	
	@Column
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	
	@Column
	public String getWorkCell() {
		return workCell;
	}
	public void setWorkCell(String workCell) {
		this.workCell = workCell;
	}
	
	@Column
	public String getWorkPager() {
		return workPager;
	}
	public void setWorkPager(String workPager) {
		this.workPager = workPager;
	}
	
	@Column
	public String getWorkFax() {
		return workFax;
	}
	public void setWorkFax(String workFax) {
		this.workFax = workFax;
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

		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((hsEmail == null) ? 0 : hsEmail.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((campusEmail == null) ? 0 : campusEmail.hashCode());
		result = prime * result + ((addrStreet == null) ? 0 : addrStreet.hashCode());
		result = prime * result + ((addrCity == null) ? 0 : addrCity.hashCode());
		result = prime * result + ((addrState == null) ? 0 : addrState.hashCode());
		result = prime * result + ((addrZip == null) ? 0 : addrZip.hashCode());
		result = prime * result + ((postalAddress == null) ? 0 : postalAddress.hashCode());
		result = prime * result + ((workPhone == null) ? 0 : workPhone.hashCode());
		result = prime * result + ((workCell == null) ? 0 : workCell.hashCode());
		result = prime * result + ((workPager == null) ? 0 : workPager.hashCode());
		result = prime * result + ((workFax == null) ? 0 : workFax.hashCode());

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

		IamContactInfo other = (IamContactInfo) obj;

		if((iamId == null) && (other.iamId != null)) {
			return false;
		} else if((iamId != null) && iamId.equals(other.iamId) == false) {
			return false;
		}

		if((email == null) && (other.email != null)) {
			return false;
		} else if((email != null) && email.equals(other.email) == false) {
			return false;
		}

		if((hsEmail == null) && (other.hsEmail != null)) {
			return false;
		} else if((hsEmail != null) && hsEmail.equals(other.hsEmail) == false) {
			return false;
		}

		if((campusEmail == null) && (other.campusEmail != null)) {
			return false;
		} else if((campusEmail != null) && campusEmail.equals(other.campusEmail) == false) {
			return false;
		}

		if((addrStreet == null) && (other.addrStreet != null)) {
			return false;
		} else if((addrStreet != null) && addrStreet.equals(other.addrStreet) == false) {
			return false;
		}

		if((addrCity == null) && (other.addrCity != null)) {
			return false;
		} else if((addrCity != null) && addrCity.equals(other.addrCity) == false) {
			return false;
		}

		if((addrState == null) && (other.addrState != null)) {
			return false;
		} else if((addrState != null) && addrState.equals(other.addrState) == false) {
			return false;
		}

		if((addrZip == null) && (other.addrZip != null)) {
			return false;
		} else if((addrZip != null) && addrZip.equals(other.addrZip) == false) {
			return false;
		}

		if((postalAddress == null) && (other.postalAddress != null)) {
			return false;
		} else if((postalAddress != null) && postalAddress.equals(other.postalAddress) == false) {
			return false;
		}

		if((workPhone == null) && (other.workPhone != null)) {
			return false;
		} else if((workPhone != null) && workPhone.equals(other.workPhone) == false) {
			return false;
		}

		if((workCell == null) && (other.workCell != null)) {
			return false;
		} else if((workCell != null) && workCell.equals(other.workCell) == false) {
			return false;
		}

		if((workPager == null) && (other.workPager != null)) {
			return false;
		} else if((workPager != null) && workPager.equals(other.workPager) == false) {
			return false;
		}

		if((workFax == null) && (other.workFax != null)) {
			return false;
		} else if((workFax != null) && workFax.equals(other.workFax) == false) {
			return false;
		}

		return true;
	}
}
