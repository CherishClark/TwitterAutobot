import java.util.Date;

public class Issue {
    private Long id;
    private Date created;
    private Date dateLastUpdated;
    private IssueType issueType;
    private String title;


    public Issue(Long id, Date created, Date dateLastUpdated, IssueType issueType, String title) {
        this.id = id;
        this.created = created;
        this.dateLastUpdated = dateLastUpdated;
        this.issueType = issueType;
        this.title = title;
    }

    public Issue() {
    }

    enum IssueType {
        BIKING("Biking"),
        CROSSWALKS("Crosswalks"),
        SIDEWALKS__RAMPS("Sidewalks"),
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

    public Long getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public String getTitle() {
        return title;
    }
}
