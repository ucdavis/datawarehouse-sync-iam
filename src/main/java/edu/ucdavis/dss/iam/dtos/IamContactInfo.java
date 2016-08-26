package edu.ucdavis.dss.iam.dtos;

public class IamContactInfo {
	private Long id;
	private Long iamId;
	private String email, hsEmail, campusEmail;
	private String addrStreet, addrCity, addrState, addrZip, postalAddress;
	private String workPhone, workCell, workPager, workFax;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIamId() {
		return iamId;
	}
	public void setIamId(Long iamId) {
		this.iamId = iamId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHsEmail() {
		return hsEmail;
	}
	public void setHsEmail(String hsEmail) {
		this.hsEmail = hsEmail;
	}
	public String getCampusEmail() {
		return campusEmail;
	}
	public void setCampusEmail(String campusEmail) {
		this.campusEmail = campusEmail;
	}
	public String getAddrStreet() {
		return addrStreet;
	}
	public void setAddrStreet(String addrStreet) {
		this.addrStreet = addrStreet;
	}
	public String getAddrCity() {
		return addrCity;
	}
	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}
	public String getAddrState() {
		return addrState;
	}
	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}
	public String getAddrZip() {
		return addrZip;
	}
	public void setAddrZip(String addrZip) {
		this.addrZip = addrZip;
	}
	public String getPostalAddress() {
		return postalAddress;
	}
	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getWorkCell() {
		return workCell;
	}
	public void setWorkCell(String workCell) {
		this.workCell = workCell;
	}
	public String getWorkPager() {
		return workPager;
	}
	public void setWorkPager(String workPager) {
		this.workPager = workPager;
	}
	public String getWorkFax() {
		return workFax;
	}
	public void setWorkFax(String workFax) {
		this.workFax = workFax;
	}
}
