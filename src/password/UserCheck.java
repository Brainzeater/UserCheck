package password;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class UserCheck extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /*Разметка*/
        GridPane grid = new GridPane();
        /*Расположение разметки*/
        grid.setAlignment(Pos.CENTER);
        /*Расстояние между ячейками*/
        grid.setHgap(5);
        grid.setVgap(5);
        /*Расстояния от краёв ячеек*/
        grid.setPadding(new Insets(25, 25, 25, 25));
        /*Кнопка авторизации*/
        Button signInButton = new Button("Sign In");
        signInButton.setPrefSize(100, 40);
        signInButton.setFont(Font.font("Franklin Gothic Medium", FontWeight.NORMAL, 20));
        /*Кнопка регистрации*/
        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefSize(100, 40);
        signUpButton.setFont(Font.font("Franklin Gothic Medium", FontWeight.NORMAL, 20));
        /*Кнопка выхода*/
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(100, 40);
        exitButton.setFont(Font.font("Franklin Gothic Medium", FontWeight.NORMAL, 20));
        /*Добавление кнопок на разметку*/
        grid.add(signInButton, 0, 0);
        grid.add(signUpButton, 0, 1);
        grid.add(exitButton, 0, 2);
        /*Действие при нажатии кнопки авторизации*/
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                /*Отключение кнопок*/
                signInButton.setDisable(true);
                signUpButton.setDisable(true);
                /*Запустить окно авторизации*/
                Stage signInStage = new Stage();
                new SignInAction(signInStage);
                /*Отслеживание закрытия окна*/
                signInStage.setOnHiding(event ->
                {
                    signInButton.setDisable(false);
                    signUpButton.setDisable(false);
                });
            }
        });
        /*Действие при нажатии кнопки регистрации*/
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                /*Отключение кнопок*/
                signInButton.setDisable(true);
                signUpButton.setDisable(true);
                /*Запустить окно авторизации*/
                Stage signUpStage = new Stage();
                new SignUpAction(signUpStage);
                /*Отслеживание закрытия окна*/
                signUpStage.setOnHiding(event ->
                {
                    signInButton.setDisable(false);
                    signUpButton.setDisable(false);
                });
            }
        });
        /*Действие при нажатии на кнопку выхода*/
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        /*Иконка окна*/
        primaryStage.getIcons().add(new Image("file:icon.png"));
        /*Создание сцены с разметкой*/
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        /*Отменить возможность изменения размера*/
        primaryStage.setResizable(false);
        /*Отслеживание закрытия окна*/
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        /*Показать окно*/
        primaryStage.show();
    }
}
