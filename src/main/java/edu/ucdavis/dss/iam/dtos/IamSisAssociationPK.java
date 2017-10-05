package edu.ucdavis.dss.iam.dtos;

import javax.persistence.Embeddable;
import java.io.Serializable;

@SuppressWarnings("serial")
@Embeddable
public class IamSisAssociationPK implements Serializable {
	protected Long iamId;
	protected String assocRank;

	public IamSisAssociationPK() {}

	public IamSisAssociationPK(Long iamId, String assocRank) {
		this.iamId = iamId;
		this.assocRank = assocRank;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assocRank == null) ? 0 : assocRank.hashCode());
		result = prime * result + ((iamId == null) ? 0 : iamId.hashCode());
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
		IamSisAssociationPK other = (IamSisAssociationPK) obj;
		if (assocRank == null) {
			if (other.assocRank != null)
				return false;
		} else if (!assocRank.equals(other.assocRank))
			return false;
		if (iamId == null) {
			if (other.iamId != null)
				return false;
		} else if (!iamId.equals(other.iamId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.iamId + "-" + this.assocRank;
	}

	public Long getIamId() {
		return this.iamId;
	}

	public void setIamId(Long iamId) {
		this.iamId = iamId;
	}

	public String getAssocRank() {
		return this.assocRank;
	}

	public void setAssocRank(String assocRank) {
		this.assocRank = assocRank;
	}
}
