import java.io.*;

public class PythonPlugin implements Plugin {
  private final String scriptPath;

  public PythonPlugin(String scriptPath) {
    this.scriptPath = scriptPath;
  }

  @Override
  public String execute(String input) {
    try {
      Process process = Runtime.getRuntime().exec("python " + scriptPath + " " + input);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
      return output.toString().trim();
    } catch (IOException e) {
      e.printStackTrace();
      return "Error while executing python plugin.";
    }
  }
}
