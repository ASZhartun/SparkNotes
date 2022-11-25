package com.example.sparknotes;

import java.util.ArrayList;

public interface ActionNoteItemListener {
	public void deleteNotes(ArrayList<Long> positions);

	public void deleteNote(Long positions);
	
	public void openNote(long id);
	
	void save(long position, String title, String content, String date, ArrayList<AttachItem> attaches);
}
