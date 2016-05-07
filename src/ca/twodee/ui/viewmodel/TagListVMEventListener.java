package ca.twodee.ui.viewmodel;

public interface TagListVMEventListener {
	public void onRename(TagVM tag, String oldName, String newName);
	public void onAdd(TagVM tag);
	public void onRemove(TagVM tag);
	//public void onCategoryChange(TagVM tag);
}
