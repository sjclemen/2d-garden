package ca.twodee.ui.helpers;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

public class DataBindingHelper {
	public static void bindTextToName(DataBindingContext context, IObservableValue selection, Text target) {
		IObservableValue nameObservable = BeanProperties.value((Class) selection.getValueType(), "name",
				String.class)
				.observeDetail(selection);
		Binding nameBinding = context.bindValue(WidgetProperties.text(SWT.NONE).observe(target), nameObservable,
				new UpdateValueStrategy(false,
						UpdateValueStrategy.POLICY_ON_REQUEST), null);

	}
}
