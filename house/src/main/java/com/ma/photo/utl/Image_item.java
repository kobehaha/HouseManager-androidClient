package com.ma.photo.utl;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;

@SuppressWarnings("serial")
public class Image_item implements Serializable {

	public String imge_id;
	public String imge_thumbnail_path;
	public String imge_path;
	private Bitmap bitmap;
	public boolean isSelected = false;

	public String getImge_id() {
		return imge_id;
	}

	public void setImge_id(String imge_id) {
		this.imge_id = imge_id;
	}

	public String getImge_thumbnail_path() {
		return imge_thumbnail_path;
	}

	public void setImge_thumbnail_path(String imge_thumbnail_path) {
		this.imge_thumbnail_path = imge_thumbnail_path;
	}

	public String getImge_path() {
		return imge_path;
	}

	public void setImge_path(String imge_path) {
		this.imge_path = imge_path;
	}

	public Bitmap getBitmap() {
		if (bitmap == null) {
			try {
				bitmap = Bimp.change_image_size(imge_path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
