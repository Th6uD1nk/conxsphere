import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;

public class FileDecryptPlugin {

  public static void main(String[] args) throws Exception {
    String inputDir = "data/file_name/";

    byte[] keyBytes = Files.readAllBytes(Paths.get(inputDir + "public_key.dat"));
    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

    int chunkNumber = 0;
    while (true) {
      String chunkFileName = inputDir + "encrypted_chunk_" + String.format("%04d", chunkNumber) + ".dat";
      if (!Files.exists(Paths.get(chunkFileName))) {
        break;
      }

      byte[] encryptedChunk = Files.readAllBytes(Paths.get(chunkFileName));
      byte[] decryptedChunk = decryptData(encryptedChunk, secretKey);

      String outputFileName = inputDir + "decrypted_chunk_" + String.format("%04d", chunkNumber) + ".dat";
      Files.write(Paths.get(outputFileName), decryptedChunk);

      System.out.println("Chunk " + chunkNumber + " decrypted and saved : " + outputFileName);
      chunkNumber++;
    }
  }

  private static byte[] decryptData(byte[] data, SecretKeySpec key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(data);
  }
}