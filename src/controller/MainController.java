
package controller;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Student;

/** 
 * Controller des Haupfensters
 * Verwaltet Studententabelle, Filter und Toolbar Aktionen wie Hinzufuegeb, Bearbeiten, Loeschen)
 */
public class MainController {
	
	
	@FXML
	private Button hinzufuegenButton;
	@FXML
	private Button bearbeitenButton;
	@FXML
	private Button loeschenButton;

	@FXML 
	private TextField suchfeld;
	
	@FXML
	private TableView<Student> studierendenTabelle;
	@FXML
	private TableColumn<Student, String> vornameSpalte;
	@FXML
	private TableColumn<Student, String> nachnameSpalte;
	@FXML
	private TableColumn<Student, String> matrikelnummerSpalte;
	@FXML 
	private TableColumn<Student, String> studiengangSpalte;
	@FXML
	private TableColumn<Student, Integer> semesterSpalte;
	@FXML
	private TableColumn<Student, String> emailSpalte;
	@FXML
	private TableColumn<Student, Double> durchschnittSpalte;
	
	//comboBox https://www.geeksforgeeks.org/java/javafx-combobox-with-examples/ 
	@FXML 
	private ComboBox<String> studiengangFilterBox;
	
	
	// was ObservableList ist hier geschaut https://www.youtube.com/watch?v=XvnJAVItaAw 
    /** Liste aller Studenten (Observable fuer automatische TableView Updates) */
	private final ObservableList<Student> studierende = FXCollections.observableArrayList();
	
	// Filter und Sorting
	//https://docs.oracle.com/javase/8/javafx/api/javafx/collections/transformation/FilteredList.html
	private FilteredList<Student> gefiltert;
	//https://docs.oracle.com/javase/8/javafx/api/javafx/collections/transformation/SortedList.html
	private SortedList<Student> sortiert;
		
	
	/**
	 * Initialisiert die Tabelle und und Filter,
	 * wird automatusch von FXMLLoader aufgerufen
	 */
	@FXML
	public void initialize() {
		//CellValueFactory: basierend auf YouTube Tutorial - https://www.youtube.com/watch?v=mtdlX2NMy4M 
		//https://openjfx.io/javadoc/17/javafx.controls/javafx/scene/control/TableColumn.html
		//https://openjfx.io/javadoc/17/javafx.base/javafx/beans/property/SimpleDoubleProperty.html
		//fuer Student Properties angepasst
		//Spalten mit Daten verbinden 
		vornameSpalte.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getVorname()));

		nachnameSpalte.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getNachname()));

		matrikelnummerSpalte.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getMatrikelnummer()));

		studiengangSpalte.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getStudiengang()));

		semesterSpalte.setCellValueFactory(cd ->
        new SimpleIntegerProperty(cd.getValue().getSemester()).asObject());

		emailSpalte.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getEmail()));

		durchschnittSpalte.setCellValueFactory(cd ->
        new SimpleDoubleProperty(cd.getValue().getDurchschnitt()).asObject());
    
    	//Filter und Sortierung einrichten
		//Idee aus YT - www.youtube.com/watch?v=2M0L6w3tMOY		
		gefiltert = new FilteredList<>(studierende, s -> true); //erstmal alle zeigen //////////////////////
		sortiert = new SortedList<Student>(gefiltert);
		
		//Ueber comparatorProperty habe ich hier geschaut - https://openjfx.io/javadoc/22/javafx.controls/javafx/scene/control/TableView.html
		sortiert.comparatorProperty().bind(studierendenTabelle.comparatorProperty());
		studierendenTabelle.setItems(sortiert);
        
        //Filter ComboBox initialisieren
		studiengangFilterBox.getItems().add("Alle");
		//ComboBox: foreach aus  https://java-blog.ru/osnovy/java-foreach 
		studierende.forEach(s -> {
        	String p = s.getStudiengang();
        	if(p != null && !p.isBlank() && !studiengangFilterBox.getItems().contains(p)) {
        		studiengangFilterBox.getItems().add(p);
        	}
        });
		//https://www.geeksforgeeks.org/java/javafx-combobox-with-examples/ (Hier Tutorim geschaut und fuer mein Fall angepasst)
		studiengangFilterBox.getSelectionModel().select("Alle"); // als standartwert machen
        
		
		suchfeld.setOnAction(e -> filterAktualisieren());
		studiengangFilterBox.setOnAction(e -> filterAktualisieren());
		filterAktualisieren();        
	}
	
    // Filter Methoden
	/** 
	 * Aktualisiert die Tabelle basierend auf Suchtext und Studiengang Filter
	 */
	//https://schulung.netlify.app/blog/javafx-8-tableview-sorting-filtering/
	//hier idee genommen fuer mein Fall angepasst
	public void filterAktualisieren() {
		String tmp = suchfeld.getText();
		if(tmp == null) {
			tmp = "";
		}
		final String text = tmp.trim().toLowerCase();
		final String studiengang = studiengangFilterBox.getValue();
		

		//für mein fall angepasst
		//Hauptquelle - https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
		//https://falconbyte.net/javafx-tableview fuer Nachname, Vorname und Matrikelnummer angepasst
		//Studiengang-Filter
		gefiltert.setPredicate(s -> { 
			if(s == null) {
				return false;
			}
			
			if(studiengang != null && !studiengang.equals("Alle")) {
				if(!studiengang.equals(s.getStudiengang())) {
					return false;
				}
			}
			
			//Textsuche in Nachname oder Matrikelnummer
			if(text.isEmpty()) {
				return true;
			}
			
			String vorname = (s.getVorname() == null) ? "" : s.getVorname().toLowerCase();
			String nachname = (s.getNachname() == null) ? "" : s.getNachname().toLowerCase();
			String matrikelnummer = (s.getMatrikelnummer() == null) ? "" : s.getMatrikelnummer().toLowerCase();
			
			return vorname.contains(text) || nachname.contains(text) || matrikelnummer.contains(text);		
		});
	}
	
	
	/** 
	 * Setzt alle Filter zurueck
	 */
	@FXML
	public void onFilterZuruecksetzen() { 
		suchfeld.setText("");
		studiengangFilterBox.setValue("Alle");
		filterAktualisieren();
	}
	
	
	
	//Tutorium : https://jenkov.com/tutorials/javafx/stage.html 
	/**
	 * Oeffnet Formular zum Hinzufuegen einers neuen Studenten 
	 */
	@FXML
	public void onStudentHinzufuegen() {
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentFormView.fxml"));
		Scene scene = new Scene(loader.load());
		
		//quelle https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html fuer mein Fall mit Dialog angepasst
		Stage dialog = new Stage();
		dialog.setTitle("Studierende anlegen");
		//APPLICATION_MODAL quelle https://stackoverflow.com/questions/31046945/javafx-stage-modality/31047035
		//idee genommen, aber fuer meinen Fall angepasst
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setScene(scene);
		dialog.showAndWait();
		
		//quelle https://stackoverflow.com/questions/54253926/getting-opened-fxmlloaders-controller hier die Loesung gefunden 
		StudentFormController form = loader.getController();
		Student erstellt = form.getErgebnis();
		if(erstellt == null) {
			return;
		}
		
		//Prueft die Matrikelnummer auf Doppelung
		if(matNrExists(erstellt.getMatrikelnummer(), null)) {
			//alert error https://www.youtube.com/watch?v=6jWb_-Y1mXo
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setTitle("Fehler");
			a.setContentText("Dieses Matrikelnummer existiert schon!!");
			a.showAndWait();
			return;
		}
		
		
		if(erstellt != null) {
			studierende.add(erstellt);
			String p = erstellt.getStudiengang();
			if(p != null && !p.isBlank() && !studiengangFilterBox.getItems().contains(p)) {
				studiengangFilterBox.getItems().add(p);
			}
			
			filterAktualisieren();
		}
		
	} catch(Exception ex) {
		//quelle https://stackoverflow.com/questions/54253926/getting-opened-fxmlloaders-controller hier die Loesung gefunden fuer mein Problem
		ex.printStackTrace();
		}
	}
	
	
	
	private void studiengaengeNeuLaden() {
		String selected = studiengangFilterBox.getValue();
		
		studiengangFilterBox.getItems().clear();
		studiengangFilterBox.getItems().add("Alle");
		
		for(int i = 0; i < studierende.size(); i++) {
			Student s = studierende.get(i);
			String sg = s.getStudiengang();
			
			if (sg != null && !sg.isBlank() && !studiengangFilterBox.getItems().contains(sg)) {
	            studiengangFilterBox.getItems().add(sg);
	        }
		}
		
		if(selected != null && studiengangFilterBox.getItems().contains(selected)) {
	        studiengangFilterBox.setValue(selected);
		} else {
	        studiengangFilterBox.setValue("Alle");
		}
		
	}
	
	/**
	 * Oeffnet Formular zum Bearbeiten des ausgewaehlten Studenten
	 */
	@FXML
	public void onStudentBearbeiten() {
		Student selected = studierendenTabelle.getSelectionModel().getSelectedItem();
		if(selected == null) {
			return;
		}
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentFormView.fxml"));
			//Loader + Edit:https://stackoverflow.com/questions/54253926/getting-opened-fxmlloaders-controller 
			Scene scene = new Scene(loader.load());
			
			StudentFormController form = loader.getController();
			form.setStudentForEdit(selected);
			
			Stage dialog = new Stage();
			dialog.setTitle("Studierenden bearbeiten");
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setScene(scene);
			
			dialog.showAndWait();
			
			
			studiengaengeNeuLaden();
			filterAktualisieren();
			studierendenTabelle.refresh();
			
			
			//hier habe ich die Loesung gefunden fuer mein Problem 
			//https://stackoverflow.com/questions/11065140/javafx-2-1-tableview-refresh-items
			studierendenTabelle.refresh(); // benutzen um Aenderungen zu zeigen 
			
		} catch (Exception ex) {
			//https://stackoverflow.com/questions/2560368/what-is-the-use-of-printstacktrace-method-in-java
			ex.printStackTrace();
		}
		
		
	}
	
	
	
	/**
	 * Loeschet den ausfewaehlten Studenten nach Bestaetigung
	 */
	@FXML
	public void onStudentLoeschen() {
		Student selected = studierendenTabelle.getSelectionModel().getSelectedItem();
		if(selected == null) {
			return;
		}
		boolean ok = bestaetigen("Löschen bestätigen", "Möchtest du wirklich löschen? - Matrikelnummer: " + selected.getMatrikelnummer());
	
		if(ok) {
			studierende.remove(selected);
		}
	}
	
	
	
	/** 
	 * Oeffnet Pruefungsfenster fuer ausgewaehlten Stundenten.
	 */
	@FXML
	public void onPruefungenOeffnen() {
		Student selected = studierendenTabelle.getSelectionModel().getSelectedItem();
		if(selected == null) {
			return;
		}
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExamView.fxml"));
			Scene scene = new Scene(loader.load());
			
			ExamController examController = loader.getController();
			examController.setStudent(selected);
			
			Stage dialog = new Stage();
			dialog.setTitle("Prüfungen");
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.setScene(scene);
			dialog.showAndWait();
			
			studierendenTabelle.refresh();
		} catch (Exception ex) {
			//https://stackoverflow.com/questions/2560368/what-is-the-use-of-printstacktrace-method-in-java
			ex.printStackTrace();
		}
		
		
	}
	
	
	//https://openjfx.io/javadoc/21/javafx.controls/javafx/scene/control/Alert.html (alert 
	//https://www.youtube.com/watch?v=KzxE3ZcSIvQ
	/** 
	 * Ziegt ein Bestaetigungdialog
	 * @param titel Titel des Dialoges
	 * @param nachricht Nachricht des Dialoges für Benutzer
	 * @return true, wenn der Benutzer bestätigt (OK), sonst false
	 */
	public boolean bestaetigen(String titel, String nachricht) {
		//Alerttype Confirmation (Hier Tutorium benutz) -  https://www.youtube.com/watch?v=61_QA_yEEtQ 
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(titel);
		alert.setContentText(nachricht);
		alert.showAndWait();
		return alert.getResult() == ButtonType.OK;
	}
	
	
	/**
	 * Prueft ob Matrikelnummer bereits existiert
	 * @param matNr zu prüfende Matrikelnummer
	 * @param ignore Student der bei Bearbeitung bei der Prüfung ignoriert wird
	 * @return true wenn Matrikelnummer bereits existiert, false sonst
	 */
	public boolean matNrExists(String matNr, Student ignore) {
		if(matNr == null) {
			return false;
		}  	
		String m = matNr.trim();
		
		for(int i = 0; i < studierende.size(); i++) {
			Student s = studierende.get(i);
			
			if(s == null) {
				continue;
			}
			if(ignore != null && s == ignore) {
				continue;
			}
			if(m.equals(s.getMatrikelnummer())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
}
