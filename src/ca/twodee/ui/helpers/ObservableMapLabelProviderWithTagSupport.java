package ca.twodee.ui.helpers;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;

import ca.twodee.ui.viewmodel.TagVM;

/**
 * Expands tags for use in the issue view.
 */
public class ObservableMapLabelProviderWithTagSupport extends ObservableMapLabelProvider {
	
	public ObservableMapLabelProviderWithTagSupport(
			IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex < attributeMaps.length) {
			Object result = attributeMaps[columnIndex].get(element);
			// TODO: detect this in a smarter way
			if (result instanceof HashSet<?>) {
				return getTagText((HashSet<?>) result);
			}
			return result == null ? "" : result.toString();
		}
		return null;
	}

	public String getTagText(HashSet<?> element) {
		Iterator<?> tagIterator = element.iterator();
		StringBuilder sb = new StringBuilder();
		while (tagIterator.hasNext()) {
			TagVM tvm = (TagVM)tagIterator.next();
			sb.append(tvm.getName());
			if (tagIterator.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

}
