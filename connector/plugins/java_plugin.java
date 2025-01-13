public class JavaPlugin implements Plugin {
  @Override
  public String execute(String input) {
    return "java plugin processed: " + input.toUpperCase();
  }
}
