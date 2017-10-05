package edu.ucdavis.dss.datawarehouse.sync.iam;

import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPpsAssociation;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO for ElasticSearch person
 * 
 * Used by GSON for JSON encoding.
 * 
 * @author christopherthielen
 *
 */
public class ESPersonDTO {
	@Getter @Setter private String iamId;
	@Getter @Setter private String dFirstName, dLastName, userId, email, dMiddleName, oFirstName, oMiddleName, oLastName, dFullName, oFullName;
	@Getter @Setter private List<IamContactInfo> contactInfos;
	@Getter @Setter private List<IamPerson> people;
	@Getter @Setter private List<IamPrikerbacct> prikerbaccts;
	@Getter @Setter private List<IamPpsAssociation> associations;
}
