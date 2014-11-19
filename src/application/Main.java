package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *example from
 * http://javatutorialhq.com/javafx/form-creation/
 */
public class Main
    extends Application
{
  File info;
  ArrayList<Date> dates;
  ArrayList<PairSchools> schools;

  @Override
  public void start(Stage primaryStage)
  {

    primaryStage.setTitle("Scheduler");
    Pane pane = new Pane();
    pane.setPadding(new Insets(25, 25, 25, 25));
    Scene scene = new Scene(pane, 300, 300);
    //Text sceneTitle = new Text("Choose"");

    Button openResource = new Button("Open School Infomation File");
    openResource.setLayoutX(50);
    openResource.setLayoutY(50);
    pane.getChildren().add(openResource);
    Button generate = new Button("Generate Schedule");
    generate.setLayoutY(200);
    generate.setLayoutX(75);

    pane.getChildren().add(generate);
    openResource.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          info = fileChooser.showOpenDialog(primaryStage);
        }
      });

    generate.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          if (info != null)
            {
              //schools = Parser.parse(info);
              //Scheduler.generate(schools)
            }

        }
      });

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be
   * launched through deployment artifacts, e.g., in IDEs with limited FX
   * support. NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }
}
