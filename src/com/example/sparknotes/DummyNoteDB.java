package com.example.sparknotes;

import java.util.ArrayList;
import java.util.Date;

import android.icu.text.SimpleDateFormat;

public class DummyNoteDB {

	public DummyNoteDB() {
		super();
	}

	public ArrayList<SparkNote> getNotes() {
		ArrayList<SparkNote> dummies = new ArrayList<SparkNote>();
		dummies.add(new SparkNote("Title1", "Day. Appear sea all created make own they're beast him earth replenish grass signs own so life earth forth under."));
		dummies.add(new SparkNote("Title2", "Yielding wherein also moved can't. Very seasons kind signs herb face Very had rule waters won't. Evening all to them."));
		dummies.add(new SparkNote("Title3", "Saw us without two also set. Had given third called, deep beast saw that one moved fourth midst. Together, divide."));
		dummies.add(new SparkNote("Title4", "Is fruit them grass fill dry over winged grass lesser cattle you'll kind yielding be gathering place abundantly divide light."));
		dummies.add(new SparkNote("Title5", "Saw won't upon over divided life evening itself given won't land male, greater wherein to forth years that every. Land."));
		dummies.add(new SparkNote("Title6", "Day his it female yielding spirit over multiply evening seasons seed, had let multiply third, bearing appear land darkness heaven."));
		return dummies;
	}

}
