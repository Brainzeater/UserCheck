package password;


import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;


public class FileHelper {
    private FileWriter myFileWriter;
    private BufferedWriter myBufferedWriter;
    private FileReader myFileReader;
    private BufferedReader myBufferedReader;
    /*Логин*/
    private String login;
    /*Пароль*/
    private String password;
    /*Список данных о пользователях*/
    private ArrayList<String> userData;
    /*Список пользователей*/
    private ArrayList<Element> users;

    public FileHelper(String login, String password) {
        this.login = login;
        this.password = password;
        userData = new ArrayList<>();
        /*Считать данные о пользователях из файла*/
        userData = readUser();
        if (userData != null) {
            /*Если файл не пустой, то заполнить список пользователей,
             *разбивая данные на соль, хэш и логин соответственно*/
            users = new ArrayList<>();
            int i = 0;
            while (i < userData.size()) {
                users.add(new Element(userData.get(i), userData.get(i + 1), userData.get(i + 2)));
                i += 3;
            }
        }
    }

    /**
     * Save User
     */
    public void writeUser() {
        try {
            myFileWriter = new FileWriter("users.txt", true);
            myBufferedWriter = new BufferedWriter(myFileWriter);
            /*Сгенерировать соль*/
            byte[] salt = generateSalt();
            /*Вычислить хэш пароля и соли*/
            String hash = encryptPassword(password, salt);
            /*Перевод в шестнадцатиричную систему*/
            String stringSalt;
            stringSalt = String.format("%064x", new java.math.BigInteger(1, salt));
            /*Запись соли, хэша и логина зарегистрировавшегося пользователя*/
            myBufferedWriter.write(stringSalt + "\n");
            myBufferedWriter.write(hash + "\n");
            myBufferedWriter.write(login + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                myBufferedWriter.flush();
                myBufferedWriter.close();
                myFileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Read Users' data
     *
     * @return massive of users' data
     */
    private ArrayList<String> readUser() {
        ArrayList<String> tempArray = new ArrayList<String>();
        try {
            myFileReader = new FileReader("users.txt");
            myBufferedReader = new BufferedReader(myFileReader);
            while (true) {
                String line = myBufferedReader.readLine();
                if (line == null) break;
                /*Разбить считанные данные по символу новой строки*/
                String[] parts = line.split("\n");
                /*Записать считанный массив в список данных о пользователях*/
                for (String str : parts) {
                    tempArray.add(str);
                }
            }
            return tempArray;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            /*Сообщение об ошибке*/
            Alert alert = new Alert(Alert.AlertType.ERROR);
            /*Название сообщения*/
            alert.setTitle("Error");
            /*Заголовок сообщения*/
            alert.setHeaderText(null);
            /*Установить текст сообщения*/
            alert.setContentText("File is not found");
            /*Без иконки*/
            alert.initStyle(StageStyle.UTILITY);
            /*Показать сообщение*/
            alert.showAndWait();
        } finally {
            try {
                myBufferedReader.close();
                myFileReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Checks if user's data is correct
     *
     * @return true if data is correct and false otherwise
     */
    public boolean authUser() {
        boolean foundUser = false;
        Element currentUser = new Element(null, null, null);
        /*Последовательный перебор пользователей и сравнение логинов*/
        for (Element el : users) {
            if (el.getUser().equals(login)) {
                /*Если логины совпадают,
                 *то зафиксировать этого пользователя*/
                foundUser = true;
                currentUser = el;
            }
        }
        if (foundUser) {
            /*Перевести соль зафиксированного пользователя в байт*/
//            byte[] currentSalt = currentUser.getSalt().getBytes(Charset.forName("UTF-8"));
            byte[] currentSalt = hexStringToByteArray(currentUser.getSalt());
            /*Вычислить хэш от пароля, введённого пользователем,
             *и соли зафиксированного пользователя*/
            String hash = encryptPassword(password, currentSalt);
            if (currentUser.getHash().equals(hash)) {
                /*Если хэш совпадает, то вывести
                 *сообщение об успешной авторизации*/
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Searches for user with same login
     *
     * @return true if same user is found and false otherwise
     */
    public boolean findUser() {
        /*Перебор пользователей*/
        for (Element el : users) {
            /*Сравнение логинов пользователей с введённым*/
            if (el.getUser().equals(login)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates random salt
     *
     * @return salt
     */
    private byte[] generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return salt;
    }

    /**
     * Hash SHA-256
     *
     * @param text String that becomes hash
     * @return hash
     */
    private String hash(String text) {
        /*Вычисление хэш-функции от пароля и соли*/
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            try {
                md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }
            byte[] digest = md.digest();
            /*Перевод в шестнадцатиричную систему*/
            return String.format("%064x", new java.math.BigInteger(1, digest));

        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts password
     *
     * @param password user's password
     * @param salt     generated salt
     * @return hash taken from password and salt
     */
    private String encryptPassword(String password, byte[] salt) {
        try {
            /*Преобразование соли в строку*/
            String stringSalt = new String(salt, "UTF-8");
            /*Вычислить хэш от пароля и соли*/
            return new String(hash(password + stringSalt));
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return null;
    }

    /**
     * (From Internet)
     * To make salt string to byte array
     * @param s salt string to become byte array
     * @return salt byte array
     */
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
