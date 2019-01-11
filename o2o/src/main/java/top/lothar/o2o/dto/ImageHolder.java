package top.lothar.o2o.dto;

import java.io.InputStream;
/**
 * 传入图片名称和图片输入流用
 * @author Lothar
 *
 */
public class ImageHolder {

	private String imageName;
	private InputStream image;

	public ImageHolder(String imageName, InputStream image) {
		this.imageName = imageName;
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}

}
