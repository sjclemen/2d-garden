package ca.twodee.ui.viewmodel;

import java.util.HashSet;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.persistence.message.issue.EditIssueRequest;
import ca.twodee.persistence.message.issue.EditIssueTagsRequest;
import ca.twodee.schema.Issue;
import ca.twodee.schema.IssueTag;
import ca.twodee.util.HashSetUtil;

public class IssueVM extends ObservableModel {
	private final static Logger logger = Logger.getLogger(IssueVM.class);
	
	private int id;
	private final ArchiveVM archiveVM;
	private String name;
	private Integer rating;
	private HashSet<TagVM> lastFlushIssueTags;
	private HashSet<TagVM> issueTags;
	private final BackendConnection conn;
	// true if any fields except issueTags have changed since either last time
	// flush or the constructor was called
	private Boolean isDirty = false;
	// ditto, except only for issueTags
	private Boolean isTagListDirty = false;
	private final Issue issue;
	
	public IssueVM(Issue issue, TagListVM tagViewModel, ArchiveListVM archiveViewModel, BackendConnection conn) {
		this.id = issue.getId();
		this.archiveVM = archiveViewModel.getArchiveById(issue.getArchive().getId());
		this.name = issue.getName();
		if (issue.getRating() != null) {
			this.rating = issue.getRating().intValue();
		}
		this.issueTags = new HashSet<TagVM>();
		this.conn = conn;
		this.issue = issue;
		if (issue.getIssueTags() == null) {
			logger.warn("Issue tags on issue " + id + " are null. Ignore this if a new item.");
		} else {
			for (IssueTag issueTag : issue.getIssueTags()) {
				this.issueTags.add(tagViewModel.getTagVMById(issueTag.getTag().getId()));
			}
		}
		this.lastFlushIssueTags = (HashSet<TagVM>) issueTags.clone();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		if (this.id != id) {
			isDirty = true;
		}
		firePropertyChange("id", this.id, this.id = id);
	}
	public ArchiveVM getArchiveVM() {
		return archiveVM;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (!this.name.equals(name)) {
			isDirty = true;
		}
		firePropertyChange("name", this.name, this.name = name);
	}
	public Integer getRating() {
		if (rating == null) {
			return 0;
		}
		return rating;
	}
	public void setRating(Integer rating) {
		if (rating == 0) {
			rating = null;
		}
		if (this.rating == null && rating != null) {
			isDirty = true;
		} else if (this.rating == null && rating == null) {
			
		} else if (!this.rating.equals(rating)) {
			isDirty = true;
		}
		firePropertyChange("rating", this.rating, this.rating = rating);
	}
	public HashSet<TagVM> getIssueTags() {
		return (HashSet<TagVM>) issueTags.clone();
	}
	
	public void setIssueTags(HashSet<TagVM> issueTags) {
		isTagListDirty = (!HashSetUtil.isEqual(this.lastFlushIssueTags, issueTags));
		firePropertyChange("issueTags", this.issueTags, this.issueTags = (HashSet<TagVM>) issueTags.clone());
	}
	
	// Flushes a tag list in two parts: the issue's items and its tags.
	public void flush() {
		logger.debug("Calling flush for " + this.name + " with dirty status " + isDirty + " tag dirty " + isTagListDirty);
		if (isDirty) {
			issue.setName(name);
			if (rating != null) {
				issue.setRating(rating.shortValue());
			}
			conn.enqueueTask(new EditIssueRequest(issue));
			isDirty = false;
		}
		if (isTagListDirty) {
			LinkedList<Integer> tagsToRemove = new LinkedList<Integer>();
			LinkedList<Integer> tagsToAdd = new LinkedList<Integer>();
			
			for (TagVM check : this.lastFlushIssueTags) {
				if (!issueTags.contains(check)) {
					tagsToRemove.add(check.getId());
				}
			}
			
			for (TagVM check : this.issueTags) {
				if (!lastFlushIssueTags.contains(check)) {
					tagsToAdd.add(check.getId());
				}
			}
			
			EditIssueTagsRequest editIssueTags = new EditIssueTagsRequest(id, tagsToRemove, tagsToAdd);
			conn.enqueueTask(editIssueTags);
			
			lastFlushIssueTags = issueTags;
			isTagListDirty = false;
		}
	}
	
	
}
