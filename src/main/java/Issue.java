import java.util.Date;

public class Issue {
    private Date dateLastUpdated;
    private IssueType issueType;

    public Issue(Date dateLastUpdated, IssueType issueType) {
        this.dateLastUpdated = dateLastUpdated;
        this.issueType = issueType;
    }

    public Issue() {
    }

    enum IssueType {
        BIKING("Biking Issues"),
        CROSSWALKS("Crosswalks"),
        SIDEWALKS__RAMPS("Sidewalks and Ramps"),
        STREET_CURB("Street / Curb");

        private String issueName;
        private Date dateLastUpdated;

        IssueType(String issueName) {
            this.issueName = issueName;
        }

        public String getIssueName() {
            return issueName;
        }
    }
    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Date getDateLastUpdated() {

        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

}
