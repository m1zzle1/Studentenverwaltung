package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Student;

/** 
 * Controller fuer das Formular-Fenster,
 * Erstellt oder bearbeitet einen Studenten durch Eingaben in Textfeldern
 */
public class StudentFormController {
	@FXML
	private TextField vornameFeld;
	@FXML
	private TextField nachnameFeld;
	@FXML
	private TextField matrikelnummerFeld;
	@FXML
	private TextField studiengangFeld;
	@FXML
	private TextField semesterFeld;
	@FXML
	private TextField emailFeld;
	
    /** Neu erstellter oder bearbeiteter Student */
	private Student ergebnis; 
	
    /** Student zum Bearbeiten (null = neu erstellen) */
	private Student bearbeitenStudent;

	
	/** 
	 * gibt das Ergebnis des Formulares zurück
	 * @return neuer Stundent oder null wenn abgebrochen
	 */
	public Student getErgebnis() {
		return ergebnis;
	}
	
	
	/** 
	 * setzt einen Studierenden zum Bearbeiten und fuellt die Eingabefelder.
	 * @param student Studierender, der bearbeitet werden soll
	 */
	public void setStudentForEdit(Student student) {
		this.bearbeitenStudent = student;
		
		vornameFeld.setText(student.getVorname());
		nachnameFeld.setText(student.getNachname());
		matrikelnummerFeld.setText(student.getMatrikelnummer());
		studiengangFeld.setText(student.getStudiengang());
		semesterFeld.setText(String.valueOf(student.getSemester()));
		emailFeld.setText(student.getEmail());
		
	}
	
	
	/**
	 * Speichert Eingaben und erstellt oder bearbeitet Studenten
	 *  Prueft Vorname, Nachname, Matrikelnummer, Semester und E-mail
	 */
	@FXML 
	public void onSpeichern() {
		String vorname = vornameFeld.getText().trim();
		String nachname = nachnameFeld.getText().trim();
		String matrikelnummer = matrikelnummerFeld.getText().trim();
		
		if(vorname.isEmpty() || nachname.isEmpty() || matrikelnummer.isEmpty()) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Fehler");
			a.setContentText("Vorname, Nachname und Matrikelnummer dürfen nicht leer sein");
			a.showAndWait();
			return;
		}
		
		//Semester pruefen
		int semester;
		try {
			semester = Integer.parseInt(semesterFeld.getText().trim());
			if(semester <= 0) {
				//NumberFormatException - https://www.geeksforgeeks.org/java/numberformatexception-in-java-with-examples/
				//machen absichtlig eine Fehler um in catch zu geraten
				throw new NumberFormatException();
			}
		} catch (Exception ex) {
			//YT Alert Error - https://www.youtube.com/watch?v=6jWb_-Y1mXo
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Fehler");
			a.setContentText("Fachsemester muss eine Zahl sein und > 0 sein");
			a.showAndWait();
			return;
		}
		
		
		
		String studiengang = studiengangFeld.getText().trim();
		String email = emailFeld.getText().trim();
		
		if(!istGueltigeEmail(email)) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Fehler");
			a.setContentText("Email ist falsch, muss @ und . ernhalten, und @ vor . sein");
			a.showAndWait();
			return;
		}
		
		
		
		//Student erstellen oder bearbeiten
		if(bearbeitenStudent != null) {
			bearbeitenStudent.setVorname(vorname); 
			bearbeitenStudent.setNachname(nachname);
			bearbeitenStudent.setMatrikelnummer(matrikelnummer);
			bearbeitenStudent.setSemester(semester);
			bearbeitenStudent.setStudiengang(studiengang);
			bearbeitenStudent.setEmail(email);
			ergebnis = bearbeitenStudent;
		} else {
			ergebnis = new Student(vorname, nachname, matrikelnummer, studiengang, semester, email);
		}
		//Close FXML , idee hier genommen, für mein Fall aber angepasst : https://stackoverflow.com/questions/13567019/close-fxml-window-by-code-javafx/18362656
		//Fenster schliessen
		Stage stage = (Stage) vornameFeld.getScene().getWindow();
		stage.close();
	} 
	
	
	/** 
	 * Schliesst Formular ohne was zu speichern
	 */
	@FXML
	public void onAbbrechen() {
		ergebnis = null;
		Stage stage = (Stage) vornameFeld.getScene().getWindow();
		stage.close();
	}	
	
	
	
	/**
	 * Email-Pruefung
	 * @param email Zu pruefende E-Mail 
	 * @return true wenn gueltig oder leer
	 */
	public boolean istGueltigeEmail(String email) {
		if(email == null || email.trim().isEmpty()) {
			return true;
		}
		String e = email.trim().toLowerCase();
		return e.contains("@") && e.contains(".") && e.indexOf("@") < e.indexOf(".");
	}
	
	
}
	
	
	
	
