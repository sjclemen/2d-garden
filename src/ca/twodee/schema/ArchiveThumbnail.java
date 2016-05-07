package ca.twodee.schema;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ARCHIVE_THUMBNAIL")
public class ArchiveThumbnail {
	public final static String ID_FN = "ID";
	public final static String THUMBNAIL_FN = "THUMBNAIL";

	@DatabaseField(columnName = ID_FN, generatedId = true)
	private int id;
	@DatabaseField(columnName = THUMBNAIL_FN, dataType=DataType.SERIALIZABLE, canBeNull = false)
	private byte[] thumbnail;

	public ArchiveThumbnail() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getThumbnail() {
		return this.thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

}
