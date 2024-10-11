package domashni.domashno1;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сървърът стартира...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиентът се свърза.");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                String request;
                while ((request = in.readLine()) != null) {
                    String[] tokens = request.split(" ");

                    if (tokens[0].equals("add") && tokens.length == 3) {
                        try {
                            String name = tokens[1];
                            int grade = Integer.parseInt(tokens[2]);
                            addStudent(name, grade);
                            out.println("Студентът е добавен успешно.");
                        } catch (NumberFormatException e) {
                            out.println("Грешка: Невалидна оценка. Моля, въведете валидно число за оценка.");
                        }
                    } else if (tokens[0].equals("add") && tokens.length != 3) {
                        out.println("Грешка: Невалидна команда. Форматът трябва да бъде: add <име> <оценка>.");
                    } else if (request.equals("list")) {
                        out.println(getAllStudents());
                    } else if (request.equals("average")) {
                        try {
                            out.println("Средната оценка е: " + getAverageGrade());
                        } catch (IOException e) {
                            out.println("Грешка: " + e.getMessage());
                        }
                    } else {
                        out.println("Грешка: Невалидна команда.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized void addStudent(String name, int grade) throws IOException {
            try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
                fw.write(name + " " + grade + "\n");
            }
        }

        private synchronized String getAllStudents() throws IOException {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        }

        private synchronized double getAverageGrade() throws IOException {
            List<Integer> grades = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(" ");
                    grades.add(Integer.parseInt(tokens[1]));
                }
            }
            return grades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        }
    }
}

