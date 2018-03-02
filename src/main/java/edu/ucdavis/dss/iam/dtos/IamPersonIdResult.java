package edu.ucdavis.dss.iam.dtos;

// Stores the output of /api/iam/people/ids
public class IamPersonIdResult {
    private String iamId;
    private String mothraId;
    private String ppsId;
    private String studentId;
    private String bannerPIdM;
    private String externalId;

    public String getIamId() {
        return iamId;
    }
    public void setIamId(String iamId) {
        this.iamId = iamId;
    }

    public String getMothraId() {
        return mothraId;
    }
    public void setMothraId(String mothraId) {
        this.mothraId = mothraId;
    }

    public String getPpsId() {
        return ppsId;
    }
    public void setPpsId(String ppsId) {
        this.ppsId = ppsId;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBannerPIdM() {
        return bannerPIdM;
    }
    public void setBannerPIdM(String bannerPIdM) {
        this.bannerPIdM = bannerPIdM;
    }

    public String getExternalId() {
        return externalId;
    }
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
