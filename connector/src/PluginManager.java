import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {
  private final List<Plugin> plugins = new ArrayList<>();

  public void loadPlugins(String pluginDir) {
    File dir = new File(pluginDir);
    if (dir.exists() && dir.isDirectory()) {
      for (File file : dir.listFiles()) {
        if (file.getName().endsWith(".jar")) {
          System.out.println("Loading java plugin: " + file.getName());
        } else if (file.getName().endsWith(".py")) {
          System.out.println("Loading python plugin: " + file.getName());
          plugins.add(new PythonPlugin(file.getAbsolutePath()));
        }
      }
    }
  }

  public String executePlugins(String input) {
    StringBuilder result = new StringBuilder(input);
    for (Plugin plugin : plugins) {
      result = new StringBuilder(plugin.execute(result.toString()));
    }
    return result.toString();
  }
}
