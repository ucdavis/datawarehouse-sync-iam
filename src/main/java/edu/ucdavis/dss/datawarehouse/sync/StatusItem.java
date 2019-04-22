package edu.ucdavis.dss.datawarehouse.sync;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "statuses" )
public class StatusItem {
	private String upstreamDb;
	private Integer duration;
	private Date lastAttempt, lastSuccess;

	@Id
	@Column(name = "upstream_db")
	public String getUpstreamDb() { return upstreamDb; }
	public void setUpstreamDb(String upstreamDb) { this.upstreamDb = upstreamDb; }

	@Column(name = "last_attempt")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastAttempt() { return lastAttempt; }
	public void setLastAttempt(Date lastAttempt) { this.lastAttempt = lastAttempt; }

	@Column(name = "last_success")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastSuccess() { return lastSuccess; }
	public void setLastSuccess(Date lastSuccess) { this.lastSuccess = lastSuccess; }

	@Column
	public Integer getDuration() { return duration; }
	public void setDuration(Integer duration) { this.duration = duration; }
}
