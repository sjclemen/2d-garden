package ca.twodee.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ARCHIVE_REPOSITORY")
public class ArchiveRepository {
	public static final String ID_FN = "ID";
	public static final String PATH_FN = "PATH";
	
	@DatabaseField(columnName = ID_FN, generatedId = true)
	private int id;
	@DatabaseField(columnName = PATH_FN, canBeNull = false)
	private String path;

	public ArchiveRepository() {
	}

	public ArchiveRepository(int id, String path) {
		this.id = id;
		this.path = path;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
