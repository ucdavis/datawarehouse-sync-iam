package edu.ucdavis.dss.datawarehouse.sync.iam;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "iam_vers" )
public class Version {
	Long id;
	private Timestamp importStarted, importFinished, vers;
	
	/**
	 * 'version' value is set on object construction but can be overridden
	 * using setVersion().
	 */
	public Version() {
		// Set nanoseconds to 0 to avoid MySQL rounding issue.
		// Details: MySQL (as of 5.7) seems to round the nanoseconds on INSERT but not on WHERE, so
		// a timestamp like "2016-05-16 16:25:04.964" will be stored as "2016-05-16 16:25:05" (rounded up)
		// but then a subsequent UPDATE ... WHERE statement will not match that timestamp
		// even with the identical "2016-05-16 16:25:04.964" value (as it is not rounded up).
		this.vers = new Timestamp(new Date().getTime());
		this.vers.setNanos(0);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="import_started")
	public Timestamp getImportStarted() {
		return importStarted;
	}

	public void setImportStarted(Timestamp importStarted) {
		this.importStarted = importStarted;
	}

	@Column(name="import_finished")
	public Timestamp getImportFinished() {
		return importFinished;
	}

	public void setImportFinished(Timestamp importFinished) {
		this.importFinished = importFinished;
	}

	@Column
	public Timestamp getVers() {
		return vers;
	}

	public void setVers(Timestamp vers) {
		this.vers = vers;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Version[id='%d', vers='%s']",
				id, vers);
	}
}
