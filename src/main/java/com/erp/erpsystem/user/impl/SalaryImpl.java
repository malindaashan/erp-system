package com.erp.erpsystem.user.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.didisoft.pgp.PGPException;
import com.didisoft.pgp.PGPLib;
import com.erp.erpsystem.pojo.FileUpload;
import com.erp.erpsystem.user.dao.FileUploadRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.bouncycastle.openpgp.PGPPublicKey;

@Service
public class SalaryImpl {

	@Value("${erp.file.save.location}")
	private String filePath;

	@Autowired
	FileUploadRepository fileRepo;
	
	static Cipher cipher;

	public String saveEmployee(HttpServletRequest request, MultipartFile file) {
		FileUpload fileUpload = new FileUpload();
		// fileUpload.setId(null);
		fileUpload.setDes(request.getParameter("description"));
		fileUpload.setName(request.getParameter("filename"));
		fileUpload.setStatus("P");
		fileRepo.save(fileUpload);
		try {
			writeBytesToFile(filePath + request.getParameter("filename") + ".xlsx", file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}

		return "Success";
	}

	private static void writeBytesToFile(String fileOutput, byte[] bytes) throws IOException {

		try (FileOutputStream fos = new FileOutputStream(fileOutput)) {
			fos.write(bytes);
		}

	}

	public List<FileUpload> getFilesFromStatus(String status) {
		List<FileUpload> fu = fileRepo.findByStatus(status);
		return fu;

	}

	public String approve(Integer id) {
		fileRepo.setById(Long.parseLong(String.valueOf(id)), "A");
		transferFile(fileRepo.getById(Long.parseLong(String.valueOf(id))));
		return "Success";
	}

	private void transferFile(FileUpload fileUpload) {
		  String SFTPUSER = "admin";
		  String SFTPHOST = "localhost";
		  int SFTPPORT = 22;
		  String SFTPPASS = "123456";
		  Session session = null;
		  Channel channel = null;
		  ChannelSftp channelSftp =null;
		  String SFTPWORKINGDIR= "/mas";
		  String transferingFilePath = filePath.replace("//", "\\")+fileUpload.getName()+".xlsx";
		  System.out.println(transferingFilePath);
		  encrypt2(transferingFilePath);
		try{ 
		 // InputStream in = encryptFile(filePath.replace("//", "\\"),fileUpload.getName()+".xlsx");	  
		  JSch jsch = new JSch();
		  session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
		  session.setPassword(SFTPPASS);
		  java.util.Properties config = new java.util.Properties();
		  config.put("StrictHostKeyChecking", "no");
		  session.setConfig(config);
		  session.connect();
		  System.out.println("Host connected.");
		  channel = session.openChannel("sftp");
		  channel.connect();
		  System.out.println("sftp channel opened and connected.");
		  channelSftp = (ChannelSftp) channel;
		  channelSftp.cd(SFTPWORKINGDIR);
		  
		  File f = new File(transferingFilePath);
		  channelSftp.put(new FileInputStream(f), f.getName());
		  System.out.println("File transfered successfully to host.");
		} catch (Exception ex) {
		  channelSftp.exit();
		  ex.printStackTrace();
		  System.out.println("sftp Channel exited.");
		  channel.disconnect();
		
		}
	}
	public void encrypt(String input){
		try{
			
			CamelContext camelContext=new DefaultCamelContext();
			camelContext.addRoutes(new RouteBuilder(){
				public void configure() throws Exception
				{
					final String publicKeyFileName="file:F:\\Keys\\Test_0x201766D3_public.asc";
					final String keyUserid="test";
					from("file:‪F:\\erpuploads\\444444.xlsx?noop=true;delete=true")
					.marshal().pgp(publicKeyFileName, keyUserid)
					.to("file:F:\\En\\OUT");
				}
			});
			camelContext.start();
			Thread.sleep(5000);
			camelContext.stop();
		} catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	public void encrypt2(String input){
		try{
			
			PGPLib pgp = new PGPLib();
			 // is output ASCII or binary
			  boolean asciiArmor = true; 
			 
			  // should integrity check information be added
			  // set to true for compatibility with GnuPG 2.2.8+
			  boolean withIntegrityCheck = false; 
			 
			  // obtain the streams
			  InputStream inStream = new FileInputStream("F:\\erpuploads\\ttt");
			  InputStream keyStream = new FileInputStream("F:\\Keys\\Test_0x201766D3_public.asc");
			  OutputStream outStream = new FileOutputStream("F:\\En\\OUT.pgp");
			 
			  // Here "INPUT.txt" is just a string to be written in the
			  // OpenPGP packet which contains:
			  // file name string, timestamp, and the actual data bytes
			  pgp.encryptStream(inStream, "ttt",
			                    keyStream,
			                    outStream,
			                    asciiArmor,
			                    withIntegrityCheck);
		} catch(Exception e ){
			e.printStackTrace();
		}
	}

	
	public static PrivateKey getPrivate()throws Exception {

		    byte[] keyBytes = Files.readAllBytes(Paths.get("F:\\Keys\\","private_key.der"));

		    PKCS8EncodedKeySpec spec =
		      new PKCS8EncodedKeySpec(keyBytes);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    return kf.generatePrivate(spec);
	}
	
	 public static PublicKey getPublic() throws Exception {			    
			    byte[] keyBytes = Files.readAllBytes(Paths.get("F:\\Keys\\","public_key.der"));

			    X509EncodedKeySpec spec =
			      new X509EncodedKeySpec(keyBytes);
			    KeyFactory kf = KeyFactory.getInstance("RSA");
			    return kf.generatePublic(spec);
	 }
	
	private InputStream encryptFile(String path, String name) throws InvalidKeyException, Exception {
		InputStream targetStream = null;
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(128); // The AES key size in number of bits
			SecretKey secKey = generator.generateKey();
			byte[] fileBytes = Files.readAllBytes(Paths.get(path, name));
			
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
			byte[] byteCipherText = aesCipher.doFinal(fileBytes);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.PUBLIC_KEY, getPublic());
			byte[] encryptedKey = cipher.doFinal(secKey.getEncoded()/*Seceret Key From Step 1*/);

			
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, getPublic());
			byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);
			targetStream = new ByteArrayInputStream(encryptedFileBytes);
			
			/***
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, getPublic());
			
			String secretMessage = "Baeldung secret message";
			byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
			byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
			
			String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
			//System.out.println("EN msg"+ encodedMessage);
			
			Cipher decryptCipher = Cipher.getInstance("RSA");
			decryptCipher.init(Cipher.DECRYPT_MODE, getPrivate());
			
			byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
			String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
	        
			//System.out.println("D msg"+ decryptedMessage);
			
	        ***/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
		return targetStream;
	}
}
