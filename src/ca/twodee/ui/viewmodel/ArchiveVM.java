package ca.twodee.ui.viewmodel;

import java.nio.file.FileSystems;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.ImageData;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.schema.Archive;

public class ArchiveVM extends ObservableModel {
	private final static Logger logger = Logger.getLogger(ArchiveVM.class);
	
	private final Integer id;
	private final Integer thumbnailId;
	private String path;
	private Boolean isError;
	private Boolean isMissing;
	private String md5Hash;
	private Integer pageCount;
	private Date lastModified;
	private final ArchiveRepositoryVM repository;
	private final BackendConnection conn;
	private final ArchiveThumbnailCache cache;
	
	public ArchiveVM(Archive a, BackendConnection conn, ArchiveThumbnailCache cache, ArchiveRepositoryListVM arlv) {
		this.id = a.getId();
		this.path = a.getPath();
		this.isError = a.isError();
		this.isMissing = a.isMissing();
		this.md5Hash = a.getMd5Hash();
		this.pageCount = a.getPageCount();
		this.lastModified = new Date(a.getLastModified());
		this.repository = arlv.getById(a.getArchiveRepository().getId());
		this.conn = conn;
		if (a.getThumbnail() != null) {
			this.thumbnailId = a.getThumbnail().getId();
		} else {
			this.thumbnailId = null;
		}
		this.cache = cache;
	}

	public Integer getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public Boolean isError() {
		return isError;
	}

	public Boolean isMissing() {
		return isMissing;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public Date getLastModified() {
		return lastModified;
	}
	
	public ImageData getThumbnailImageData() {
		if (thumbnailId != null) {
			try {
				return cache.getImageData(thumbnailId);
			} catch (ExecutionException e) {
				logger.error("Failed to retrieve thumbnail", e);
			}
		}
		return cache.getDefaultImageData();
	}
	
	public String getFullPath() {
		return FileSystems.getDefault().getPath(repository.getPath(), path).toString();
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof ArchiveVM && ((ArchiveVM) o).getId() == id);
	}
	
}
