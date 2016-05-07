package ca.twodee.ui.viewmodel;

import ca.twodee.schema.TagCategory;

public class TagCategoryVM extends ObservableModel {
	private final Integer id;
	private final TagCategory model;
	private String name;

	public TagCategoryVM(TagCategory tc) {
		this.id = tc.getId();
		this.model = tc;
		this.name = tc.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public int getId() {
		return id;
	}

	public TagCategory getModel() {
		return model;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof TagCategoryVM && ((TagCategoryVM) o).getId() == id);
	}
	
	@Override
	public String toString() {
		return "TagCategory: " + id + " " + name;
	}

}
