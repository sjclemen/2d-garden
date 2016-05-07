package ca.twodee.persistence.manager;

import java.sql.SQLException;
import java.util.HashMap;

import ca.twodee.schema.Archive;
import ca.twodee.schema.ArchiveRepository;
import ca.twodee.schema.ArchiveThumbnail;
import ca.twodee.schema.Issue;
import ca.twodee.schema.IssueTag;
import ca.twodee.schema.Tag;
import ca.twodee.schema.TagCategory;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

/**
 * The DAO cacher caches ORM DAOs on a per-thread level for each thread in the backend's
 * thread pool. On creation, it creates *all* the DAOs.
 */
public class DaoCacher {
	private final HashMap<Class<?>, Dao <?, ?> > daos = new HashMap<>();
	
	public DaoCacher(ConnectionSource cs) throws SQLException {
		daos.put(Archive.class, DaoManager.createDao(cs, Archive.class));
		daos.put(ArchiveRepository.class, DaoManager.createDao(cs, ArchiveRepository.class));
		daos.put(ArchiveThumbnail.class, DaoManager.createDao(cs, ArchiveThumbnail.class));
		daos.put(Issue.class, DaoManager.createDao(cs, Issue.class));
		daos.put(IssueTag.class, DaoManager.createDao(cs, IssueTag.class));
		daos.put(Tag.class, DaoManager.createDao(cs, Tag.class));
		daos.put(TagCategory.class, DaoManager.createDao(cs, TagCategory.class));
		
		for (Dao <?, ?> dao : daos.values()) {
			dao.setObjectCache(false);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Dao<?, ?> getDao(Class c) {
		return daos.get(c);
	}
	
	@SuppressWarnings("unchecked")
	public Dao<Archive, Integer> getArchiveDao() {
		return (Dao<Archive, Integer>) daos.get(Archive.class);
	}
	
	@SuppressWarnings("unchecked")
	public Dao<ArchiveRepository, Integer> getArchiveRepositoryDao() {
		return (Dao<ArchiveRepository, Integer>) daos.get(ArchiveRepository.class);
	}
	
	@SuppressWarnings("unchecked")
	public Dao<ArchiveThumbnail, Integer> getArchiveThumbnailDao() {
		return (Dao<ArchiveThumbnail, Integer>) daos.get(ArchiveThumbnail.class);
	}
	
	@SuppressWarnings("unchecked")
	public Dao<Issue, Integer> getIssueDao() {
		return (Dao<Issue, Integer>) daos.get(Issue.class);
	}
	
	@SuppressWarnings("unchecked")
	public Dao<IssueTag, Integer> getIssueTagDao() {
		return (Dao<IssueTag, Integer>) daos.get(IssueTag.class);
	}
	
	@SuppressWarnings("unchecked")
	public Dao<Tag, Integer> getTagDao() {
		return (Dao<Tag, Integer>) daos.get(Tag.class);
	}

	@SuppressWarnings("unchecked")
	public Dao<TagCategory, Integer> getTagCategoryDao() {
		return (Dao<TagCategory, Integer>) daos.get(TagCategory.class);
	}
	
}
