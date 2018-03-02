package edu.ucdavis.dss.iam.dtos;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "iam_contactinfo" )
public class IamContactInfo {
	private Long iamId;
	private String email, hsEmail, campusEmail;
	private String addrStreet, addrCity, addrState, addrZip, postalAddress;
	private String workPhone, workCell, workPager, workFax;
	private Date createdAt, updatedAt;
	
	@Id
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

	@PreUpdate
	private void beforeUpdate() {
		this.updatedAt = new Date();
	}

	@PrePersist
	private void beforeCreation() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
}
