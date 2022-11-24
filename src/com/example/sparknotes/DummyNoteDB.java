package com.example.sparknotes;

import java.util.ArrayList;
import java.util.Date;

import android.icu.text.SimpleDateFormat;

public class DummyNoteDB {
	ArrayList<SparkNote> dummies = new ArrayList<SparkNote>();
	public DummyNoteDB() {
		super();
		fillNotes();
	}
	
	private void fillNotes() {
		dummies.add(new SparkNote("Title1", "Day. Appear sea all created make own they're beast him earth replenish grass signs own so life earth forth under."));
		dummies.add(new SparkNote("Title2", "Yielding wherein also moved can't. Very seasons kind signs herb face Very had rule waters won't. Evening all to them."));
		dummies.add(new SparkNote("Title3", "Saw us without two also set. Had given third called, deep beast saw that one moved fourth midst. Together, divide."));
		dummies.add(new SparkNote("Title4", "Is fruit them grass fill dry over winged grass lesser cattle you'll kind yielding be gathering place abundantly divide light."));
		dummies.add(new SparkNote("Title5", "Saw won't upon over divided life evening itself given won't land male, greater wherein to forth years that every. Land."));
		dummies.add(new SparkNote("Title6", "Day his it female yielding spirit over multiply evening seasons seed, had let multiply third, bearing appear land darkness heaven."));
	}

	public ArrayList<SparkNote> getNotes() {
		return dummies;
	}
	
	public SparkNote getNoteById(int id) {
		return dummies.get(id);
	}
	
	public ArrayList<SparkNote> getDeletingNotes() {
		ArrayList<SparkNote> dummies = new ArrayList<SparkNote>();
		dummies.add(new SparkNote("Deleting1", "Day. Appear sea all created make own they're beast him earth replenish grass signs own so life earth forth under."));
		dummies.add(new SparkNote("Deleting2", "Yielding wherein also moved can't. Very seasons kind signs herb face Very had rule waters won't. Evening all to them."));
		dummies.add(new SparkNote("Deleting3", "Saw us without two also set. Had given third called, deep beast saw that one moved fourth midst. Together, divide."));
		dummies.add(new SparkNote("Deleting4", "Is fruit them grass fill dry over winged grass lesser cattle you'll kind yielding be gathering place abundantly divide light."));
		dummies.add(new SparkNote("Deleting5", "Saw won't upon over divided life evening itself given won't land male, greater wherein to forth years that every. Land."));
		dummies.add(new SparkNote("Deleting6", "Day his it female yielding spirit over multiply evening seasons seed, had let multiply third, bearing appear land darkness heaven."));
		return dummies;
	}
	
	public ArrayList<ReferencePosition> getRefPositions() {
		ArrayList<ReferencePosition> dummies = new ArrayList<ReferencePosition>();
		dummies.add(new ReferencePosition("Кнопка `Экспорт`", "Служит для формирование архив-файла для последующей передачи куда-либо."));
		dummies.add(new ReferencePosition("Кнопка `Импорт`", "Позволяет создать заметку на базе выбранного каталога или архива."));
		dummies.add(new ReferencePosition("Кнопка `Корзина`", "Отображает ранее удаленные заметки. Можно восстановить заметки (поместить в основной список), либо окончательно удалить из памяти телефона."));
		dummies.add(new ReferencePosition("Кнопка `Расширенный поиск`", "Открывает окно поиска. Можно искать заметку по заголовку или по содержимому. Также можно указать интервал создания заметки."));
		return dummies;
	}
	
	public void updateNote(int position, String title, String content) {
		try {
			SparkNote sparkNote = dummies.get(position);
			sparkNote.setTitle(title);
			sparkNote.setContent(content);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
