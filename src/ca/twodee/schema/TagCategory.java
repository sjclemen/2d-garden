package ca.twodee.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "TAG_CATEGORY")
public class TagCategory {
	public final static String ID_FN = "ID";
	public final static String NAME_FN = "NAME";

	@DatabaseField(columnName = ID_FN, generatedId = true)
	private int id;
	@DatabaseField(columnName = NAME_FN, canBeNull = false)
	private String name;

	public TagCategory() {
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

}
