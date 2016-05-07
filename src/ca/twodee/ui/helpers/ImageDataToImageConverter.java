package ca.twodee.ui.helpers;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * {@link ImageData} to {@link Image} converter used in data binding.
 */
public class ImageDataToImageConverter implements IConverter {
	private final Device device;
	
	public ImageDataToImageConverter(Device device) {
		this.device = device;
	}

	@Override
	public Object getFromType() {
		return ImageData.class;
	}

	@Override
	public Object getToType() {
		return Image.class;
	}

	@Override
	public Object convert(Object fromObject) {
		return new Image(device, (ImageData)fromObject);
	}

}
