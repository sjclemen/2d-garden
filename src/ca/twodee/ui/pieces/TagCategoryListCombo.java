package ca.twodee.ui.pieces;

import java.util.ArrayList;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ca.twodee.ui.viewmodel.TagCategoryListVM;
import ca.twodee.ui.viewmodel.TagCategoryVM;

import com.google.common.base.Optional;

public class TagCategoryListCombo {
	protected final Combo tagCategoryEntry;
	private TagCategoryListVM tagCategoryListVm;
	private ComboViewer comboViewer;

	public TagCategoryListCombo(Composite parent, int style) {
		tagCategoryEntry = new Combo(parent, style);
	}
	
	public Control getControl() {
		return tagCategoryEntry;
	}
	
	private void addTagCategoryInternal(String text) {
		tagCategoryListVm.addTagCategory(text);
	}
	
	public void bindToViewModel(TagCategoryListVM viewModel) {
		this.tagCategoryListVm = viewModel;
		comboViewer = new ComboViewer(tagCategoryEntry);
		ViewerSupport.bind(comboViewer, tagCategoryListVm.getAsSet(), BeanProperties.value("name"));
		
		TagCategoryListContentProposalProvider proposalProvider = new
				TagCategoryListContentProposalProvider(viewModel);
		ContentProposalAdapter proposalAdapter = new ContentProposalAdapter(
				tagCategoryEntry, new ComboContentAdapter(), proposalProvider, null, null);
		proposalAdapter.setPropagateKeys(true);
		proposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
		proposalAdapter.addContentProposalListener(new IContentProposalListener() {

			@Override
			public void proposalAccepted(IContentProposal userSelectedProposal) {
				if (proposalProvider.getMakeNewProposal() == userSelectedProposal) {
					// TODO: migrate this to a popup which better consults the user
					addTagCategoryInternal(userSelectedProposal.getContent());
				}
			}

		});

	}
	
	public Optional<TagCategoryVM> getSelectedItem() {
		if (comboViewer.getSelection().isEmpty()) {
			return tagCategoryListVm.getByName(tagCategoryEntry.getText());
		}
		IStructuredSelection selection = (IStructuredSelection) comboViewer.getSelection();
		TagCategoryVM tagCategory = (TagCategoryVM) selection.getFirstElement();
		return Optional.of(tagCategory);
	}
	
	private static class TagCategoryListContentProposalProvider implements IContentProposalProvider {
		private final TagCategoryListVM tagCategoryListVm;
		private ContentProposal makeNewProposal;
		
		public TagCategoryListContentProposalProvider(TagCategoryListVM tagCategoryListVm) {
			this.tagCategoryListVm = tagCategoryListVm;
		}
				
		public ContentProposal getMakeNewProposal() {
			return makeNewProposal;
		}
		
		@Override
		public IContentProposal[] getProposals(String contents, int index) {
			ArrayList<ContentProposal> proposals = new ArrayList<>();
			boolean exactMatch = false;
			// look for prefix matches. is slow.
			for (Object o : tagCategoryListVm.getAsSet()) {
				TagCategoryVM tagCategory = (TagCategoryVM)o;
				String nameLowercase = tagCategory.getName().toLowerCase();
				String contentsLowercase = contents.toLowerCase();
				if (nameLowercase.indexOf(contentsLowercase) == 0) {
					if (nameLowercase.equals(contentsLowercase)) {
						exactMatch = true;
					}
					proposals.add(new ContentProposal(tagCategory.getName()));
				}
			}
			if (!exactMatch) {
				ContentProposal makeNew = new ContentProposal(contents,
						"Create new tag category: " + contents, "Creates a new tag");
				makeNewProposal = makeNew;

				proposals.add(makeNew);
			}
			return proposals.toArray(new ContentProposal[0]);
		}
		
	}
	
}
