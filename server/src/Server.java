import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  private static final int PORT = 8080;
  private static final List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Server started on port: " + PORT);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New connector at: " + clientSocket.getInetAddress());
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clients.add(clientHandler);
        new Thread(clientHandler).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void broadcast(String message, ClientHandler sender) {
    for (ClientHandler client : clients) {
      if (client != sender) {
        client.sendMessage(message);
      }
    }
  }
}
