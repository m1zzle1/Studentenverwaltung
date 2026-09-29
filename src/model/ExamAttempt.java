package model;

import java.time.LocalDate;


/** 
 * Eine Pruefungleistung und ein Versuch zu einer Pruefung
 * Enhault Modulname, VersuchNummer, Note und Datum
 * */
public class ExamAttempt {
	
	private String modulname;
	private int versuchsnummer;
	private double note;
	private LocalDate datum;
	
	
	/**
	 * Erstellt neuen Pruefungsversuch
	 * 
	 * @param modulname Modulname , wie z.B. Mathe 1
	 * @param versuchsnummer VersuchNummer, wie 1,2,3...
	 * @param note Note , wie 1.0 bis 5.0
	 * @param datum Pruefungsdatum
	 * 
	 *  */
	public ExamAttempt(String modulname, int versuchsnummer, double note, LocalDate datum) {
		this.modulname = modulname;
		this.versuchsnummer = versuchsnummer;
		this.note = note;
		this.datum = datum;
	}
	
	//Getter Methoden
	/**
	 *Gibt Modulname zurueck
	 *@return Modulname
	 **/
	public String getModulname() {
		return modulname;
	}
	
	/**
	 *Gibt VersuchNummer zurueck
	 *@return Versuchsnummer
	 **/
	public int getVersuchsnummer() {
		return versuchsnummer;
	}
	
	/**
	 *Gibt Note zurueck
	 *@return Note
	 **/
	public double getNote() {
		return note;
	}
	
	/**
	 *Gibt Datum zurueck
	 *@return Datum
	 **/
	public LocalDate getDatum() {
		return datum;
	}
	
	/**
	 *String Repraesentation fuer ListView
	 *So sieht aus : z.B "Modul - Versuch 1 - Note 4.1 - Datum 2026-02-22"
	 **/
	 @Override
	public String toString() {
		return modulname + " - Versuch " + versuchsnummer + " - Note " + note + " - Datum " + datum; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
