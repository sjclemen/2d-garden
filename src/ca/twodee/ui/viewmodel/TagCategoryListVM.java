package ca.twodee.ui.viewmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.WritableSet;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.persistence.message.tag.AddTagCategoryRequest;
import ca.twodee.schema.TagCategory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class TagCategoryListVM {
	private final Map<Integer, TagCategoryVM> tagCategoryIdToValue = new HashMap<>();
	private final WritableSet tagCategorySet = new WritableSet();
	private final BackendConnection conn;
	
	public TagCategoryListVM(List <TagCategory> tagCategoryModels, BackendConnection conn) {
		for (TagCategory tc : tagCategoryModels) {
			addTagCategoryVm(new TagCategoryVM(tc));
		}
		this.conn = conn;
	}
	
	public WritableSet getAsSet() {
		return tagCategorySet;
	}
		
	public TagCategoryVM getById(Integer id) {
		return tagCategoryIdToValue.get(id);
	}
	
	public ListenableFuture<TagCategoryVM> addTagCategory(String name) {
		TagCategory tc = new TagCategory();
		tc.setName(name);
		AddTagCategoryRequest request = new AddTagCategoryRequest(tc);
		Function<TagCategory, TagCategoryVM> addTagCategoryToVm = new Function<TagCategory, TagCategoryVM>() {
			@Override
			public TagCategoryVM apply(TagCategory input) {
				TagCategoryVM tagCategoryVm = new TagCategoryVM(input);
				addTagCategoryVm(tagCategoryVm);
				return tagCategoryVm;
			}
		};
		ListenableFuture<TagCategory> backendFuture = conn.enqueueTask(request);
		return Futures.transform(backendFuture, addTagCategoryToVm, conn.getExecutor());
	}
	
	protected void addTagCategoryVm(TagCategoryVM tagCategoryVm) {
		tagCategoryIdToValue.put(tagCategoryVm.getId(), tagCategoryVm);
		tagCategorySet.add(tagCategoryVm);
	}
	
	public Optional<TagCategoryVM> getByName(String name) {
		Preconditions.checkNotNull(name);
		Set<Entry<Integer, TagCategoryVM>> entrySet = tagCategoryIdToValue.entrySet();
		for (Entry<Integer, TagCategoryVM> tagCategoryEntry : entrySet) {
			if (name.equals(tagCategoryEntry.getValue().getName())) {
				return Optional.of(tagCategoryEntry.getValue());
			}
		}
		return Optional.absent();
	}

}
