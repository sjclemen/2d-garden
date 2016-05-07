package ca.twodee.eventbus.message;

import ca.twodee.schema.Archive;
import ca.twodee.schema.Issue;

public class CreateIssueAndArchiveMessage {
	public final Issue issue;
	public final Archive archive;
	
	public CreateIssueAndArchiveMessage(Issue issue, Archive archive) {
		this.issue = issue;
		this.archive = archive;
	}
}
