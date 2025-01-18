package plugins.test.upload;

import plugins.upload.FileUploadPlugin;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileUploadPluginTest {
  @Test
  public void testUploadFile() {
    FileUploadPlugin plugin = new FileUploadPlugin();
    String outputDir = plugin.uploadFile("plugins/data/testfile.txt");
    assertNotNull(outputDir, "Upload should return a valid output directory.");
  }
}
