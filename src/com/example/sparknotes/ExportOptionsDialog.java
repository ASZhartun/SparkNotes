package com.example.sparknotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ExportOptionsDialog extends DialogFragment {

	public interface ExportOptionsDialogListener {
		public void onExportDialogPositiveClick(DialogFragment dialog);

		public void onExportDialogNegativeClick(DialogFragment dialog);
	}

	ExportOptionsDialogListener listener;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listener = (ExportOptionsDialogListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement ExportOptionsDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Делимся заметками").setMessage("Как будем делиться?")
				.setPositiveButton("Текст", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onExportDialogPositiveClick(ExportOptionsDialog.this);

					}
				}).setNegativeButton("Zip", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onExportDialogNegativeClick(ExportOptionsDialog.this);

					}
				}).setCancelable(false);

		return builder.create();
	}

}
