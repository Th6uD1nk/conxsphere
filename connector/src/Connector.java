import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Connector {
  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 8080;
  private static final PluginManager pluginManager = new PluginManager();

  public static void main(String[] args) {
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Scanner scanner = new Scanner(System.in)) {

      pluginManager.loadPlugins("plugins");
      new Thread(() -> {
        try {
          String serverMessage;
          while ((serverMessage = in.readLine()) != null) {
            String processedMessage = pluginManager.executePlugins(serverMessage);
            System.out.println("Processed message: " + processedMessage);
          }
        } catch (IOException e) {
          System.out.println("Disconnected from server.");
        }
      }).start();

      while (true) {
        String userInput = scanner.nextLine();
        String processedInput = pluginManager.executePlugins(userInput);
        out.println(processedInput);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
