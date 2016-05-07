package ca.twodee.ui.viewmodel;


import ca.twodee.schema.Tag;

public class TagVM extends ObservableModel {
	private final int id;
	private String name;
	private TagCategoryVM tagCategory;
	
	public TagVM(Tag t, TagCategoryVM tagCategory) {
		this.id = t.getId();
		this.name = t.getName();
		this.tagCategory = tagCategory;
	}
	
	// for testing
	public TagVM(Integer id) {
		this.id = id;
	}
	
	public TagVM(Integer id, String name, TagCategoryVM tc) {
		this.id = id;
		this.name = name;
		this.tagCategory = tc;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	public TagCategoryVM getTagCategory() {
		return tagCategory;
	}
	public void setTagCategory(TagCategoryVM tagCategory) {
		firePropertyChange("tagCategory", this.tagCategory,
				this.tagCategory = tagCategory);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof TagVM && ((TagVM) o).getId() == id);
	}
	
	@Override
	public String toString() {
		return "TagVM: " + id + " " + name + " " + tagCategory.toString();
	}

}
