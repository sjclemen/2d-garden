package ca.twodee.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TAG")
public class Tag {
	public final static String ID_FN = "ID";
	public final static String NAME_FN = "NAME";
	public final static String TAG_CATEGORY_FN = "CATEGORY_ID";

	@DatabaseField(columnName = ID_FN, generatedId = true)
	private int id;
	@DatabaseField(columnName = NAME_FN, canBeNull = false)
	private String name;
	@DatabaseField(columnName = TAG_CATEGORY_FN, foreign = true, canBeNull = false)
	private TagCategory tagCategory;

	public Tag() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TagCategory getTagCategory() {
		return tagCategory;
	}

	public void setTagCategory(TagCategory tagCategory) {
		this.tagCategory = tagCategory;
	}

}
