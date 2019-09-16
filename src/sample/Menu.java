package sample;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu {

    public void openMenu(Stage primaryStage){
        Pane root = new Pane();
        Image menuImage = new Image("menu.jpg");
        ImageView img = new ImageView(menuImage);
        img.setFitHeight(800);
        img.setFitWidth(1200);
        root.getChildren().add(img);


        MenuItem newGame = new MenuItem("NEW GAME");
        MenuItem records = new MenuItem("RECORDS TABLE");
        MenuItem exitGame = new MenuItem("EXIT");
        SubMenu mainMenu = new SubMenu(newGame,records,exitGame);

        GridPane table = new GridPane();
        Table.makeTable(table);
        MenuItem back = new MenuItem("BACK");
        VBox recordsTable = new VBox();
        recordsTable.setSpacing(30);
        recordsTable.setTranslateX(400);
        recordsTable.setTranslateY(150);
        recordsTable.getChildren().addAll(table, back);

        MenuBox menuBox = new MenuBox(mainMenu);

        newGame.setOnMouseClicked(event -> {
            Game game = new Game();
            game.playNewGame(primaryStage);
        });
        records.setOnMouseClicked(event -> menuBox.setSubMenu((recordsTable)));
        exitGame.setOnMouseClicked(event ->System.exit(0));
        back.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));
        root.getChildren().addAll(menuBox);

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
