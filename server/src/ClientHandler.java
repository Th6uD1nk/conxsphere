import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public ClientHandler(Socket socket) {
    this.clientSocket = socket;
    try {
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String message) {
    out.println(message);
  }

  @Override
  public void run() {
    try {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        System.out.println("Message received: " + inputLine);
        Server.broadcast(inputLine, this);
      }
    } catch (IOException e) {
      System.out.println("Connector disconnected.");
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
