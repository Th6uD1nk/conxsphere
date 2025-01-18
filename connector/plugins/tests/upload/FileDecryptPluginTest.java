package plugins.test.upload;

import plugins.upload.FileDecryptPlugin;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileDecryptPluginTest {
  @Test
  public void testDecryptFiles() {
    try {
      String inputDir = "plugins/data/testfile.txt/";
      FileDecryptPlugin.decryptFiles(inputDir);
      assertTrue(true, "Decryption should complete successfully.");
    } catch (Exception e) {
      fail("Decryption failed with exception: " + e.getMessage());
    }
  }
}
