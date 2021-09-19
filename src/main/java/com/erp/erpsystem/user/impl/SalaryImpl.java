package com.erp.erpsystem.user.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.didisoft.pgp.PGPLib;
import com.erp.erpsystem.pojo.FileUpload;
import com.erp.erpsystem.user.dao.FileUploadRepository;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class SalaryImpl {

	@Value("${erp.file.save.location}")
	private String filePath;

	@Autowired
	FileUploadRepository fileRepo;
	
	static Cipher cipher;
	
	@Value("${sftp.host}")
	private String sftpHost;
	
	@Value("${sftp.port}")
	private int sftpPort;
	
	@Value("${sftp.user}")
	private String sftpUser;

	@Value("${sftp.password}")
	private String sftpPass;

	public String saveEmployee(HttpServletRequest request, MultipartFile file) {
		FileUpload fileUpload = new FileUpload();
		// fileUpload.setId(null);
		fileUpload.setDes(request.getParameter("description"));
		fileUpload.setName(request.getParameter("filename"));
		fileUpload.setStatus("P");
		fileUpload.setApprovedDate("0000-00-00 00:00:00");
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
		List<FileUpload> fu = null;
		if("A".equalsIgnoreCase(status)){
			fu = fileRepo.findAll();

		}else{
			
			fu = fileRepo.findByStatus(status);
		}
		return fu;

	}

	public String approve(Integer id) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		fileRepo.setById(Long.parseLong(String.valueOf(id)), "A", sdf1.format(timestamp));
		transferFile(fileRepo.getById(Long.parseLong(String.valueOf(id))));
		return "Success";
	}

	private void transferFile(FileUpload fileUpload) {
		  String SFTPUSER = sftpUser;
		  String SFTPHOST = sftpHost;
		  int SFTPPORT = sftpPort;
		  String SFTPPASS = sftpPass;
		  Session session = null;
		  Channel channel = null;
		  ChannelSftp channelSftp =null;
		  String SFTPWORKINGDIR= "/mas";
		  String transferingFilePath = filePath.replace("//", "\\")+fileUpload.getName()+".xlsx";
		  System.out.println(transferingFilePath);
		  encrypt(transferingFilePath);
		  //decrypt(transferingFilePath);
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
		  
		  //File f = new File("F:\\En\\OUT.pgp");
		  //File f = new File("F:\\En\\OUT.pgp");
		  String pgpPath = transferingFilePath.replace(".xlsx",".pgp");
		  File f = new File(pgpPath);
		  channelSftp.put(new FileInputStream(f), f.getName());
		  System.out.println("File transfered successfully to host.");
		} catch (Exception ex) {
		  channelSftp.exit();
		  ex.printStackTrace();
		  System.out.println("sftp Channel exited.");
		  channel.disconnect();
		
		}
	}
	
	public void encrypt(String transferingFilePath){
		try{
			
			PGPLib pgp = new PGPLib();
			 // is output ASCII or binary
			  boolean asciiArmor = true; 
			 
			  // should integrity check information be added
			  // set to true for compatibility with GnuPG 2.2.8+
			  boolean withIntegrityCheck = false; 
			 
			  // obtain the streams
			  //InputStream inStream = new FileInputStream("F:\\erpuploads\\444444.xlsx");
			  String pgpPath = transferingFilePath.replace(".xlsx",".pgp");
			  InputStream inStream = new FileInputStream(transferingFilePath);
			  InputStream keyStream = new FileInputStream("F:\\Keys\\Test_0x201766D3_public.asc");
			 // OutputStream outStream = new FileOutputStream("F:\\En\\OUT.pgp");
			  OutputStream outStream = new FileOutputStream(pgpPath);
			 
			  // Here "INPUT.txt" is just a string to be written in the
			  // OpenPGP packet which contains:
			  // file name string, timestamp, and the actual data bytes
			  pgp.encryptStream(inStream, "ttt",
			                    keyStream,
			                    outStream,
			                    asciiArmor,
			                    withIntegrityCheck);
			  inStream.close();
			  keyStream.close();
			  outStream.close();
		} catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	public void decrypt(String input){
		try{
			
			 PGPLib pgp = new PGPLib();
			 
			  // obtain an encrypted data stream
			  InputStream encryptedStream = new FileInputStream("F:\\En\\OUT.pgp");
			 
			  InputStream privateKeyStream = new FileInputStream("F:\\Keys\\Test_0x201766D3_SECRET.asc");
			  String privateKeyPassword = "";
			 
			  // specify the destination stream of the decrypted data
			  OutputStream decryptedStream = new FileOutputStream("‪‪C:\\Users\\malin\\Desktop\\xxxx.xlsx");
			 
			  pgp.decryptStream(encryptedStream,
			  	            privateKeyStream,
			        	    privateKeyPassword,
			        	    decryptedStream);
			
		} catch(Exception e ){
			e.printStackTrace();
		}
	}

}
