package ca.twodee.tagfilling.plugin.data;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class LookupResponseP {
	private final String name;
	private final String jName;
	private ImmutableList<TagP> tags;
	
	public LookupResponseP(String name, String jName, List<TagP> tags) {
		this.name = name;
		this.jName = jName;
		this.tags = ImmutableList.copyOf(tags);
	}
	
	public String getName() {
		return name;
	}
	public String getjName() {
		return jName;
	}
	public List<TagP> getTags() {
		return tags;
	}
	
	
}
