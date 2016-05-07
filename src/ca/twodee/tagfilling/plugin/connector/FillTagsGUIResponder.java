package ca.twodee.tagfilling.plugin.connector;

import ca.twodee.tagfilling.plugin.data.IssueP;

public interface FillTagsGUIResponder {
	public void onLoad(IssueP issue, FillTagsGUITools tools);
	public void onUnload(IssueP issue, FillTagsGUITools tools);
}
