package controller;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.ExamAttempt;
import model.Student;

/**
 * Controller fuer das Pruefungsfenster eines Studierenden,
 *  Zeigt Versuche an und erlaubt Hinzufuegen und Loeschen
 */
public class ExamController {

	@FXML
	private Label titelLabel;
	 
	//listview, diese Tutorial benutzt https://www.youtube.com/watch?v=Pqfd4hoi5cc
	@FXML 
	private ListView<ExamAttempt> versuchsListe;
	@FXML
	private Button versuchLoeschenButton;
	
	/**Aktueller Student, Wird von MainController gesetzt */
	private Student student;
	
	
	//quelle https://www.youtube.com/watch?v=XvnJAVItaAw - fuer View Updates angepasst
	// ObservableList
	/**
	 * Liste fuer ListView,
	 *  Benutzen Observable fuer automatische Updates
	 */
	private final ObservableList<ExamAttempt> viewDate = FXCollections.observableArrayList();
	
	
	//Basierend auf YT - https://www.youtube.com/watch?v=WunCf-8ob3g (tutorium) 
	/**
	 * Lädt Studentendaten in ListView
	 * @param s Der Student für den Prüfungen angezeigt werden
	 */
	public void setStudent(Student s) {
		student = s;
		titelLabel.setText("Prüfungen: " + s.getVorname() + " " + s.getNachname());
		viewDate.setAll(s.getVersuche()); // Daten kopieren
		versuchsListe.setItems(viewDate); // ListView anzeigen
	}
	
	
	/**
	 *Neuer Pruefungsversuch hizufuegen,
	 * Fragt Module, Note und Datum ab
	 */
	@FXML
	public void onVersuchHinzufuegen() {
		if(student == null) {
			return;
		}
		
		String module = frageText("Neuer Versuch", "Wie lautet Modulname: ");
		if(module == null) {
			return ;
		}
		String noteStr = frageText("Neuer Versuch", "Note (z.B. 1.0 - 5.0): ");
		if(noteStr == null) {
			return;
		}
		
		double note;
		try {
			note = Double.parseDouble(noteStr);
			if(note < 1.0 || note > 5.0) {
				new Alert(Alert.AlertType.ERROR, "Note muss zwischen 1.0 und 5.0 liegen").showAndWait();
				return;
			}
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR, "Note muss ein Zahl sein!").showAndWait();
			return;
		}
		
		LocalDate datum = frageDatum("Neuer Versuch");
		if(datum == null) {
			return;
		}
		
		int naechsterVersuch = student.getVersuche().size() + 1;
		ExamAttempt a = new ExamAttempt(module, naechsterVersuch, note, datum);
		
		student.getVersuche().add(a); //in StudentenListe hinzufuegen
		viewDate.add(a); // in ListView hinzufuegen
	}
	
	
	/**
	 *  Ausgewaehlten Versuch loeschen 
	 *  Wird Bestätigung gezeigt
	 */
	@FXML
	public void onVersuchLoeschen() {
		if(student == null) {
			return;
		}
		
		ExamAttempt selected = versuchsListe.getSelectionModel().getSelectedItem();
		if(selected == null) {
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Willst du wirklich diesen Versuch löschen?", ButtonType.OK, ButtonType.CANCEL);
		alert.showAndWait();
		ButtonType antwort = alert.getResult();
		
		if(antwort != null && antwort == ButtonType.OK) {
			student.getVersuche().remove(selected);
			viewDate.remove(selected);
		}
	}
	
	/** 
	 * Fenster schliessen 
	 */
	@FXML
	public void onSchliessen() {
		Stage stage = (Stage) versuchsListe.getScene().getWindow();
		stage.close();
	}
	
	
	// Helper Methods
	/**
	 * Dialog zum Datum waelen, nutzen dafuer DatePicker
	 *  
	 *  @param titel Dialog-Title
	 *  @return gewaehlte LocalDate oder null
	 */
	public LocalDate frageDatum(String titel) {
		//basierend auf YouTube: https://www.youtube.com/watch?v=A6HeGb7K9Gw
		//datePicker https://openjfx.io/javadoc/12/javafx.controls/javafx/scene/control/DatePicker.html , auch  docs.oracle fuer DatePicker benutz
		//localDate.now() https://www.tutorialspoint.com/javatime/javatime_localdate_now.html
		DatePicker datePicker = new DatePicker(LocalDate.now());
		//https://www.youtube.com/watch?v=ZWWvJgNLslk
		//fuer dialog habe diese Information benutz : https://falconbyte.net/javafx-alert-und-dialog-boxen
		Dialog<LocalDate> dialog = new Dialog<LocalDate>();
		dialog.setTitle(titel);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialog.getDialogPane().setContent(datePicker);
		dialog.setResultConverter(bt -> {
			if(bt == ButtonType.OK) {
				return datePicker.getValue();
			}
			return null;
		});
		
		dialog.showAndWait();
		return dialog.getResult();
	}
	

	/**
	 * Text Dialog mit Modulname und Note,
	 *  Gibt null zurueck bei Cancel oder wenn auf Kreuz gedrueckt wird
	 *  Zeigt Text-Eingabe-Dialog.
	 *  @param titel Dialog-Titel
	 *  @param label Eingabe-Hinweis  
	 *  @return Eingabe-Text oder null bei Abbruch
	 */
	public String frageText(String titel, String label) {
		//Quelle: YT - https://youtu.be/DeOXAnyCEhM?si=tBoF_xS7PZRdAj4_
		// mit trim() + empty check erweitert
		TextInputDialog dlg = new TextInputDialog();
		dlg.setTitle(titel);
		dlg.setContentText(label);
		
		dlg.showAndWait();
		if(dlg.getResult() == null) {
			return null;
		}
		
		String text = dlg.getResult().trim();
		if (text.isEmpty()) {
		    return null;
		}
		return text;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 	
	
}
