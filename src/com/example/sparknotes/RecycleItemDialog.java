package com.example.sparknotes;

import com.example.sparknotes.ExportOptionsDialog.ExportOptionsDialogListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class RecycleItemDialog extends DialogFragment {
	public interface RecycleItemDialogListener {
		public void onRecycleDialogPositiveClick(DialogFragment dialog, long id);
		public void onRecycleDialogNegativeClick(DialogFragment dialog, long id);
		public void onRecycleDialogNeutralClick(DialogFragment dialog);
	}
	
	long currentID;
	RecycleItemDialogListener listener;

	public RecycleItemDialog(long id) {
		currentID = id;
	}

	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);

		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			listener = (RecycleItemDialogListener) context;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(context.toString() + " must implement ExportOptionsDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose operation").setMessage("Destroy or restore?")
				.setPositiveButton("Restore", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onRecycleDialogPositiveClick(RecycleItemDialog.this, currentID);

					}
				}).setNegativeButton("Destroy", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onRecycleDialogNegativeClick(RecycleItemDialog.this, currentID);

					}
				}).setNeutralButton("Do nothing",  new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onRecycleDialogNeutralClick(RecycleItemDialog.this);

					}
				}).setCancelable(false);

		return builder.create();
	}
}
