package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** 
 *Hauptklass fuer das StudentApp
 *Startet Hauptfenster von FXML MainView.fxml 
 */
public class App extends Application {
	/** Startet JavaFX und zeigt das Haupfenster an
	 * 
	 *@param stage Hauptfenster 
	 */
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
		Scene scene = new Scene(loader.load(), 850, 500);
		
		stage.setTitle("StudentApp");
		stage.setScene(scene);
		stage.show();
		
	}
	/**Programmstart
	 * @param args Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
