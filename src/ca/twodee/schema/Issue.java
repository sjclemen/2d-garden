package ca.twodee.schema;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ISSUE")
public class Issue {
	public final static String ID_FN = "ID";
	public final static String ARCHIVE_ID_FN = "ARCHIVE_ID";
	public final static String NAME_FN = "NAME";
	public final static String RATING_FN = "RATING";
	
	@DatabaseField(columnName = ID_FN, generatedId = true)
	private int id;
	@DatabaseField(columnName = ARCHIVE_ID_FN, foreign = true, canBeNull = false)
	private Archive archive;
	@DatabaseField(columnName = NAME_FN, canBeNull = false)
	private String name;
	@DatabaseField(columnName = RATING_FN)
	private Short rating;
	@ForeignCollectionField(eager = true)
	private ForeignCollection<IssueTag> issueTags;

	public Issue() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Archive getArchive() {
		return archive;
	}

	public void setArchive(Archive archive) {
		this.archive = archive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getRating() {
		return this.rating;
	}

	public void setRating(Short rating) {
		this.rating = rating;
	}

	public ForeignCollection<IssueTag> getIssueTags() {
		return issueTags;
	}

	public void setIssueTags(ForeignCollection<IssueTag> issueTags) {
		this.issueTags = issueTags;
	}

}
