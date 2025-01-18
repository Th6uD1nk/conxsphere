import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;

public class FileUploadPlugin {

  public static final int CHUNK_SIZE = 1024 * 1024;
  private static final String ENCRYPTION_ALGORITHM = "AES";

  public String uploadFile(String filePath) {
    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
      throw new IllegalArgumentException("The specified file does not exist or is invalid.");
    }

    String outputDir = "data/" + file.getName() + "/";
    createOutputDirectory(outputDir);

    SecretKey secretKey = generateAESKey();
    if (secretKey == null) {
      throw new RuntimeException("Failed to generate AES key.");
    }

    String publicKeyFilePath = outputDir + "public_key.dat";
    savePublicKey(secretKey, publicKeyFilePath);
    System.out.println("Public key saved: " + publicKeyFilePath);

    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] buffer = new byte[CHUNK_SIZE];
      int bytesRead;
      int chunkNumber = 0;

      while ((bytesRead = fis.read(buffer)) > 0) {
        byte[] chunkData = new byte[bytesRead];
        System.arraycopy(buffer, 0, chunkData, 0, bytesRead);

        byte[] encryptedChunk = encryptData(chunkData, secretKey);
        if (encryptedChunk == null) {
          throw new RuntimeException("Failed to encrypt chunk " + chunkNumber + ".");
        }

        String chunkFileName = outputDir + "encrypted_chunk_" + String.format("%04d", chunkNumber) + ".dat";
        Files.write(Paths.get(chunkFileName), encryptedChunk);

        System.out.println("Chunk " + chunkNumber + " encrypted and saved: " + chunkFileName);
        chunkNumber++;
      }

      System.out.println("File successfully chunked and encrypted in directory: " + outputDir);
      return outputDir;
    } catch (IOException e) {
      throw new RuntimeException("An error occurred while processing the file: " + e.getMessage());
    }
  }

  private void createOutputDirectory(String outputDir) {
    File directory = new File(outputDir);
    if (!directory.exists()) {
      if (directory.mkdirs()) {
        System.out.println("Output directory created: " + outputDir);
      } else {
        throw new RuntimeException("Failed to create output directory: " + outputDir);
      }
    }
  }

  private SecretKey generateAESKey() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
      keyGen.init(128);
      return keyGen.generateKey();
    } catch (Exception e) {
      System.out.println("Error generating AES key: " + e.getMessage());
      return null;
    }
  }

  private void savePublicKey(SecretKey secretKey, String filePath) {
    try {
      byte[] keyBytes = secretKey.getEncoded();
      Files.write(Paths.get(filePath), keyBytes);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save public key: " + e.getMessage());
    }
  }

  private byte[] encryptData(byte[] data, Key key) {
    try {
      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return cipher.doFinal(data);
    } catch (Exception e) {
      System.out.println("Error encrypting data: " + e.getMessage());
      return null;
    }
  }
}
