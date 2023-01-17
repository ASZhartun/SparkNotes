package com.example.sparknotes;

import java.util.ArrayList;

public interface ActionNoteItemListener {
	public void shareSelectedNotesAsZip(ArrayList<Long> positions);

	public void shareSelectedNotesByApps(ArrayList<Long> selections);

	public void deleteNotes(ArrayList<Long> positions);

	public void deleteNote(Long positions);

	public void openNote(long id);

	public void openDeletedNote(long id);

	public void restoreNotes(ArrayList<Long> positions);

	void save(long position, String title, String content, String date, ArrayList<AttachItem> attaches);

	void clearSearchResult();

	public void fullDeleteNotes(ArrayList<Long> selectingItemIDs);

	public void showExportDialog();

	public void showRecycleDialog(long id);

}
