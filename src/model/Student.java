package model;

import java.util.ArrayList;
import java.util.List;



/** ModellKlasse fuer einen Studierenden
 * Enthaelt Basisdaten und Liste der Pruefungsversuche
 */
public class Student {
	private String vorname;
	private String nachname;
	private String matrikelnummer;
	private String studiengang;
	private int semester;
	private String email;
	
	
	/** 
	 * Erstellt neuen Studenten 
	 * 
	 * @param vorname Vorname.
	 * @param nachname Nachname.
	 * @param matrikelnummer Matrikelnummer
	 * @param studiengang Studiengang.
	 * @param semester Fachsemester
	 * @param email E-mail-Adresse
	*/
	public Student(String vorname, String nachname, String matrikelnummer, String studiengang, int semester, String email) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.matrikelnummer = matrikelnummer;
		this.studiengang = studiengang;
		this.semester = semester;
		this.email = email;
	}
	
	//Getter Methoden fuer Basisdaten
	/** 
	 * Gibt Vornamen zurueck
	 * @return Vorname
	 */
	public String getVorname() {
		return vorname;
	}
	
	/** 
	 * Gibt Nachname zurueck
	 * @return Nachname
	 */
	public String getNachname() {
		return nachname;
	}
	
	/** 
	 * Gibt Matrikelnummer zurueck
	 * @return Matrikelnummer
	  */
	public String getMatrikelnummer() {
		return matrikelnummer;
	}
	
	/** 
	 * Gibt Studiengang zurueck
	 * @return Studiengang 
	 */
	public String getStudiengang() {
		return studiengang;
	}
	
	/** 
	 * Gibt Semester zurueck
	 * @return Semester 
	 */
	public int getSemester() {
		return semester;
	}
	
	/** 
	 * Gibt Email zurueck
	 * @return Email 
	 */
	public String getEmail() {
		return email;
	}
	
	//Setter Methoden fuer Basisdaten
	/**
	 * Setzt Vornamen
	 * @param vorname Neuer Vorname
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	/**
	 * Setzt Nachname
	 * @param nachname Neuer Nachname
	 */
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	/**
	 * Setzt Matrikelnummer
	 * @param matrikelnummer Neuer Matrikelnummer
	 */
	public void setMatrikelnummer(String matrikelnummer) {
		this.matrikelnummer = matrikelnummer;
	}
	/**
	 * Setzt Studiengang
	 * @param studiengang Neuer Studiengang
	 */
	public void setStudiengang(String studiengang) {
		this.studiengang = studiengang;
	}
	/**
	 * Setzt Fachsemester
	 * @param semester Neuer Semester
	 */
	public void setSemester(int semester) {
		this.semester = semester;
	}
	/**
	 * Setzt E-Mail
	 * @param email Neuer E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	

	//Pruefungsversuche
	//Quelle - https://youtu.be/wsTSREgCE5E?si=NoFUXNfgnq2wEIyE 
	// fuer ExamAttempt Liste angepasst
	/**
	 * Liste von allen Pruefungsversuche
	 */
	private final List<ExamAttempt> versuche = new ArrayList<ExamAttempt>();
	
	/** 
	 * gibt alle Pruefungsversuche zurueck
	 * @return Versuche
	 */
	public List<ExamAttempt> getVersuche() {
		return versuche;
	}
	
	
	/** 
	 * Berechnet den aktuellen Notendurchschnitt ueber alle Versuche
	 * 
	 * @return Durchschnitt oder 0.0 wenn keine Noten gibt
	 */
	public double getDurchschnitt() {
		if(versuche.isEmpty()) {
			return 0.0;
		}
		double sum = 0;
		for(int i = 0; i < versuche.size(); i++) {
			sum += versuche.get(i).getNote();
		}
		return sum / versuche.size();
	}
	
	
	
	
	
	
	
	
}
