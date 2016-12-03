package password;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignInAction extends Application {

    /*Логин*/
    private String login;
    /*Пароль*/
    private String password;
    /*Текстовое поле логина*/
    private TextField userTextField;
    /*Поле ввода пароля*/
    private PasswordField pwBox;
    /*Поля сообщений*/
    private Text loginActionTarget;
    private Text pwActionTarget;

    public SignInAction(Stage signInStage) {
        this.start(signInStage);
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
        /*Расстояние от краёв ячеек*/
        grid.setPadding(new Insets(25, 25, 25, 25));
        /*Стиль текста 1*/
        Font firstFont = Font.font("Franklin Gothic Medium", FontWeight.NORMAL, 20);
        /*Стиль текста 2*/
        Font secondFont = Font.font("Consolas", FontWeight.NORMAL, 20);
        /*Ярлык ввода логина*/
        Label userName = new Label("Login:");
        userName.setFont(firstFont);
        grid.add(userName, 0, 0);
        /*Текстовое поле ввода логина*/
        userTextField = new TextField();
        userTextField.setFont(secondFont);
        userTextField.setPrefSize(300, 40);
        grid.add(userTextField, 1, 0);
        /*Текст сообщения*/
        loginActionTarget = new Text();
        grid.add(loginActionTarget, 1, 1);
        /*Ярлык ввода пароля*/
        Label pw = new Label("Password:");
        pw.setFont(firstFont);
        grid.add(pw, 0, 2);
        /*Поле ввода пароля*/
        pwBox = new PasswordField();
        pwBox.setPrefSize(300, 40);
        pwBox.setFont(secondFont);
        grid.add(pwBox, 1, 2);
        /*Текст сообщения*/
        pwActionTarget = new Text();
        grid.add(pwActionTarget, 1, 3);
        /*Кнопка подтверждения*/
        Button done = new Button("Sign In");
        done.setFont(firstFont);
        done.setPrefSize(100, 40);
        grid.add(done, 1, 4);
        /*Действие при нажатии на кнопку*/
        done.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /*Получить введённый логин*/
                login = userTextField.getText();
                /*Получить введённый пароль*/
                password = pwBox.getText();
                /*При корректном вводе данных закрыть окно*/
                if (checkData(login, password)) {
                    primaryStage.close();
                }
            }
        });
        /*Иконка окна*/
        primaryStage.getIcons().add(new Image("file:icon.png"));
        /*Создание сцены с разметкой*/
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        /*Отменить возможность изменения размера*/
        primaryStage.setResizable(false);
        /*Показать окно*/
        primaryStage.show();
    }

    /**
     * Checks data for correctness
     *
     * @param login    User Login
     * @param password User Password
     * @return true in case if data is correct
     */
    public boolean checkData(String login, String password) {
        if (login != null && !login.equals("")) {
            if (password != null && !password.equals("")) {
                FileHelper registration = new FileHelper(login, password);
                if (registration.authUser()) {
                    /*Если авторизация прошла успешно, оторбазить сообщение*/
                    showAlert("Authorization successful");
                    return true;
                } else {
                    /*Иначе отобразить сообщение о неверных данных,
                     *не уточняя, что именно было введено неверно*/
                    showAlert("Incorrect login or passport");
                    return false;
                }
            } else {
                /*Если поле пароля пустое,
                 *то запросить пароль*/
                pwActionTarget.setFill(Color.FIREBRICK);
                pwActionTarget.setText("Enter password");
                return false;
            }
        } else {
            /*Если поле логина пустое,
             *то запросить логин*/
            loginActionTarget.setFill(Color.FIREBRICK);
            loginActionTarget.setText("Enter login");
            return false;
        }
    }

    /**
     * Shows Alert with some message
     *
     * @param message message for alert
     */
    public void showAlert(String message) {
        /*Информационное сообщение*/
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        /*Название сообщения*/
        alert.setTitle("Information Dialog");
        /*Заголовок сообщения*/
        alert.setHeaderText(null);
        /*Установить текст сообщения*/
        alert.setContentText(message);
        /*Без иконки*/
        alert.initStyle(StageStyle.UTILITY);
        /*Показать сообщение*/
        alert.showAndWait();
    }
}
