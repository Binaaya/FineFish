package com.FineFish.utility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * EncryptDecrypt class provides methods for encrypting and decrypting data.
 * Uses AES encryption with PBKDF2WithHmacSHA256 for key generation.
 */
public class EncryptDecrypt {
    
    private static final String SECRET_KEY = "finefish_super_secret_key";
    private static final String SALT = "finefish_salt_value!!!!";

    /**
     * Encrypts a string using AES encryption.
     *
     * @param strToEncrypt the string to encrypt
     * @return encrypted string in Base64 format
     */
    public static String encrypt(String strToEncrypt) {
        try {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.err.println("Error while encrypting: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypts a string that was encrypted with the encrypt method.
     *
     * @param strToDecrypt the string to decrypt (in Base64 format)
     * @return decrypted string
     */
    public static String decrypt(String strToDecrypt) {
        try {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.err.println("Error while decrypting: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts a password for storage in the database.
     *
     * @param password the password to encrypt
     * @return encrypted password
     */
    public static String encryptPassword(String password) {
        return encrypt(password);
    }

    /**
     * Checks if a provided password matches a stored encrypted password.
     *
     * @param providedPassword the password to check
     * @param storedPassword the stored encrypted password
     * @return true if passwords match, false otherwise
     */
    public static boolean checkPassword(String providedPassword, String storedPassword) {
        try {
            String decryptedStoredPassword = decrypt(storedPassword);
            return providedPassword.equals(decryptedStoredPassword);
        } catch (Exception e) {
            System.err.println("Error checking password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
