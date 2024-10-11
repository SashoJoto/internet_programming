package domashni.domashno1;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Свързани сте със сървъра.");
            System.out.println("Можете да изпратите следните команди:");
            System.out.println("1. add <име> <оценка> - Добавяне на студент");
            System.out.println("2. list - Преглед на всички оценки");
            System.out.println("3. average - Изчисляване на средна оценка");

            String request;
            while ((request = userInput.readLine()) != null) {
                out.println(request);
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

