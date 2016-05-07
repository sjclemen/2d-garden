package ca.twodee.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ARCHIVE")
public class Archive {
	public final static String ID_FN = "ID";
	public final static String REPOSITORY_FN = "REPOSITORY_ID";
	public final static String PATH_FN = "PATH";
	public final static String IS_ERROR_FN = "IS_ERROR";
	public final static String IS_MISSING_FN = "IS_MISSING";
	public final static String MD5_HASH_FN = "MD5_HASH";
	public final static String PAGE_COUNT_FN = "PAGE_COUNT";
	public final static String THUMBNAIL_FN = "THUMBNAIL_ID";
	public final static String LAST_MODIFIED_FN = "LAST_MODIFIED";
	
	@DatabaseField(columnName = ID_FN, generatedId = true)
	private int id;
	@DatabaseField(columnName = REPOSITORY_FN, foreign = true, canBeNull = false)
	private ArchiveRepository archiveRepository;
	@DatabaseField(columnName = PATH_FN, canBeNull = false)
	private String path;
	@DatabaseField(columnName = IS_ERROR_FN, canBeNull = false)
	private boolean isError;
	@DatabaseField(columnName = IS_MISSING_FN, canBeNull = false)
	private boolean isMissing;
	@DatabaseField(columnName = MD5_HASH_FN, canBeNull = false)
	private String md5Hash;
	@DatabaseField(columnName = PAGE_COUNT_FN)
	private Integer pageCount;
	@DatabaseField(columnName = THUMBNAIL_FN, foreign = true, canBeNull = true)
	private ArchiveThumbnail thumbnail;
	@DatabaseField(columnName = LAST_MODIFIED_FN, canBeNull = false)
	private long lastModified;
	// TODO: add RESCAN column

	public Archive() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public ArchiveRepository getArchiveRepository() {
		return archiveRepository;
	}

	public void setArchiveRepository(ArchiveRepository archiveRepository) {
		this.archiveRepository = archiveRepository;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isMissing() {
		return isMissing;
	}

	public void setMissing(boolean isMissing) {
		this.isMissing = isMissing;
	}

	public String getMd5Hash() {
		return this.md5Hash;
	}

	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}

	public Integer getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public ArchiveThumbnail getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(ArchiveThumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}


}
