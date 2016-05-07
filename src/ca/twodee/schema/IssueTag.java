package ca.twodee.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ISSUE_TAG")
public class IssueTag {
	public final static String ID_FN = "ID";
	public final static String ISSUE_ID_FN = "ISSUE_ID";
	public final static String TAG_ID_FN = "TAG_ID";
	
	@DatabaseField(columnName = ID_FN, generatedId = true)
	private Integer id;
	@DatabaseField(columnName = ISSUE_ID_FN, foreign = true, canBeNull = false)
	private Issue issue;
	@DatabaseField(columnName = TAG_ID_FN, foreign = true, canBeNull = false)
	private Tag tag;
	
	public IssueTag() {
		
	}
	
	public IssueTag(Issue issue, Tag tag) {
		this.issue = issue;
		this.tag = tag;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Issue getIssue() {
		return issue;
	}
	
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
}
