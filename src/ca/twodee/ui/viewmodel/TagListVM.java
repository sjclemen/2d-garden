package ca.twodee.ui.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.jface.fieldassist.ContentProposal;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.backend.manager.BackendListenableFutureTask;
import ca.twodee.persistence.message.tag.AddTagRequest;
import ca.twodee.schema.Tag;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class TagListVM {
	private final WritableMap tagMap = new WritableMap(Integer.class, TagVM.class);
	private final HashMap<String, TagVM> nameToTag = new HashMap<>();;
	private final PatriciaTrie<ContentProposal> contentProposals = new PatriciaTrie<>();
	private final ArrayList<TagListVMEventListener> eventListeners = new ArrayList<>();
	private final TagCategoryListVM tagCategoryViewModel;
	private final BackendConnection conn;
	
	public TagListVM(List <Tag> tags, TagCategoryListVM tagCategoryViewModel, BackendConnection conn) {
		this.tagCategoryViewModel = tagCategoryViewModel;
		this.conn = conn;
		for (Tag t: tags) {
			TagVM tvm = new TagVM(t, tagCategoryViewModel.getById(t.getTagCategory().getId()));
			addTagVm(tvm);
		}
	}

	public PatriciaTrie<ContentProposal> getContentProposals() {
		return contentProposals;
	}
	
	public TagVM getTagVMById(Integer id) {
		return (TagVM)tagMap.get(id);
	}
	
	public TagVM getTagVMByName(String name) {
		return nameToTag.get(name);
	}
	
	// ADD NEW HANDLER: add/remove/rename/change category
	
	// work out how events will be propagated
	// who will save new tags? what format will they be in?
	// what about backend-initiated changes? do we consider those?
//	public void addTag(Tag tag) {
//		
//	}
	
	public ListenableFuture<TagVM> addTag(String name, TagCategoryVM tagCategory) {
		Tag newTag = new Tag();
		newTag.setName(name);
		newTag.setTagCategory(tagCategory.getModel());
				
		AddTagRequest atr = new AddTagRequest(newTag);
		Function<Tag, TagVM> addTagToVm = new Function<Tag, TagVM>() {
			@Override
			public TagVM apply(Tag input) {
				TagVM tagVm = new TagVM(input, tagCategoryViewModel.getById(input.getTagCategory().getId()));
				addTagVm(tagVm);
				return tagVm;
			}
		};
		BackendListenableFutureTask<Tag> enqueued = conn.enqueueTask(atr);
		return Futures.transform(enqueued, addTagToVm, conn.getExecutor());
	}
	
	private void addTagVm(TagVM tvm) {
		// TBD: validate that name isn't duplicated
		tagMap.put(tvm.getId(), tvm);
		nameToTag.put(tvm.getName(), tvm);
		contentProposals.put(tvm.getName().toLowerCase(), new ContentProposal(tvm.getName()));
		
		for (TagListVMEventListener eventListener : eventListeners) {
			eventListener.onAdd(tvm);
		}
	}
	
//	public void removeTag(Tag tag) {
//		// remove here or in DB first???
//	}
	
	private void removeTagVm(TagVM tvm) {
		contentProposals.remove(tvm.getName().toLowerCase());
		nameToTag.remove(tvm.getName(), tvm);
		tagMap.remove(tvm.getId());
		
		for (TagListVMEventListener eventListener : eventListeners) {
			eventListener.onRemove(tvm);
		}
	}
	
	public void addListener(TagListVMEventListener eventListener) {
		eventListeners.add(eventListener);
	}
	
	public void removeListener(TagListVMEventListener eventListener) {
		eventListeners.remove(eventListener);
	}
	
	public String[] getNamesSorted() {
		String[] names = nameToTag.keySet().toArray(new String[0]);
		Arrays.sort(names);
		return names;
	}
	
}
