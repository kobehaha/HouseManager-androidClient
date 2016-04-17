package com.se.house;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.special.ResideMenuDemo.R;

public class DialogModify extends Dialog {// 重写dialog

	public DialogModify(Context context) {
		super(context);
	}

	public DialogModify(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String positiveButtonText;
		private String negativeButtonText;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		@SuppressWarnings("deprecation")
		public DialogModify create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final DialogModify dialog = new DialogModify(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_seller_modify, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title

			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.btn_modify_houses))// 确认按钮代替修改房源信息
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.btn_modify_houses))// 
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.btn_modify_houses).setVisibility(
						View.GONE);
			}

			
			//取消设置的更改信息
			// set the cancel button
//			if (negativeButtonText != null) {
//				((Button) layout.findViewById(R.id.btn_modify_info))
//						.setText(negativeButtonText);
//				if (negativeButtonClickListener != null) {
//					((Button) layout.findViewById(R.id.btn_modify_info))
//							.setOnClickListener(new View.OnClickListener() {
//								public void onClick(View v) {
//									negativeButtonClickListener.onClick(dialog,
//											DialogInterface.BUTTON_NEGATIVE);
//								}
//							});
//				}
//			} else {
//				// if no confirm button just set the visibility to GONE
//				layout.findViewById(R.id.btn_modify_info).setVisibility(
//						View.GONE);
//			}

			dialog.setContentView(layout);
			return dialog;
		}
	}

}
