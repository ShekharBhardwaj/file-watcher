package com.syncodus.filewatcher.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;


@Component
public class DropBoxsdk {
	
	private static Logger LOG = LoggerFactory.getLogger(DropBoxsdk.class);
	
	@Autowired
	Environment env;
	
	 public void boxdk(String filepath) throws IOException{
		 try {
	        // Get your app key and secret from the Dropbox developers website.
		    final String APP_KEY = env.getProperty("dropbox.app.key");
	        final String APP_SECRET = env.getProperty("dropbox.app.secret");

	        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

	        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
	            Locale.getDefault().toString());
	        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

	        // Have the user sign in and authorize your app.
	        String authorizeUrl = webAuth.start();
	        LOG.info("1. Go to: " + authorizeUrl);
	        LOG.info("2. Click \"Allow\" (you might have to log in first)");
	        LOG.info("3. Copy the authorization code.");
	        //String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

	        // This will fail if the user enters an invalid authorization code.
	        //DbxAuthFinish authFinish = webAuth.finish(code);
	        String accessToken = env.getProperty("dropbox.access.token");

	        DbxClient client = new DbxClient(config, accessToken);

	        LOG.info("Linked account: " + client.getAccountInfo().displayName);

	        File inputFile = new File(filepath);
	        FileInputStream inputStream = new FileInputStream(inputFile);
	        try {
	        	client.createFolder("/email/12-12-12/txt");
	       
	            DbxEntry.File uploadedFile = client.uploadFile("/email/12-12-12/txt/"+inputFile.getName(),DbxWriteMode.add(), inputFile.length(), inputStream);
	            LOG.info("Uploaded: " + uploadedFile.toString());
	        } catch (IOException e) {
	        	LOG.error(e.getMessage());
				e.printStackTrace();
			} finally {
	            inputStream.close();
	        }

	        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
	        LOG.info("Files in the root path:");
	        for (DbxEntry child : listing.children) {
	            System.out.println("	" + child.name + ": " + child.toString());
	        }

//	        FileOutputStream outputStream = new FileOutputStream("magnum-opus.txt");
//	        try {
//	            DbxEntry.File downloadedFile = client.getFile("/magnum-opus.txt", null,
//	                outputStream);
//	            System.out.println("Metadata: " + downloadedFile.toString());
//	        } finally {
//	            outputStream.close();
//	        }
		 } catch (DbxException | FileNotFoundException ex){
			 
			 LOG.error(ex.getMessage());
			 
		 }
	    }
		 

}
