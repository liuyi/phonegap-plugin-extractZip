/*
 	Author: Vishal Rajpal
 	Filename: ExtractZipFilePlugin.java
 	Created Date: 21-02-2012
 	Modified Date: 04-04-2012

 	Author: Liu yi

 */

package com.ourbrander.pgPlugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;



public class ExtractZip extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

		if(action.equals("extract")){

			this.extract(args.getString(0),args.getString(1),callbackContext);
			return true;

		}

		return false;
	}


	public void extract(String filename,String targetFolder, CallbackContext callbackContext){
		Extract runnable =new Extract(filename,targetFolder,callbackContext);
		cordova.getThreadPool().execute(runnable);
	}

	public class Extract implements Runnable{
		private String filename="";
		private String targetFolder="";
		private CallbackContext callbackContext=null;

		public Extract(String filename,String targetFolder, CallbackContext callbackContext){
			if(filename.indexOf("file:///")==0){filename=filename.replace("file:///", "");}
			if(targetFolder.indexOf("file:///")==0){targetFolder=targetFolder.replace("file:///", "");}

			this.filename=filename;
			this.targetFolder=targetFolder;


			this.callbackContext=callbackContext;
		}
		@Override
		public void run() {

			// TODO Auto-generated method stub

			 
			int len=0;
			if(targetFolder==null ||targetFolder.length()==0){
				targetFolder=filename;
				len=-1;
			}else{
				File outFolder=new File(targetFolder);
				if(outFolder.exists() ==false){
					outFolder.mkdirs();
				}
			}

			File file=new File(filename);
			String[] dirToSplit=targetFolder.split(File.separator);
			String dirToInsert="";
			len+=dirToSplit.length;
			for(int i=0;i<len;i++)
			{
				dirToInsert+=dirToSplit[i]+File.separator;

			}

			
			

			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile;
			try {
				zipfile = new ZipFile(file);
				Enumeration<? extends ZipEntry> e = zipfile.entries();
				while (e.hasMoreElements()) 
				{
					entry = (ZipEntry) e.nextElement();
					is = new BufferedInputStream(zipfile.getInputStream(entry));
					int count;
					byte data[] = new byte[102222];
					String fileName = dirToInsert+"/" +entry.getName();

					File outFile = new File(fileName);
					if (entry.isDirectory()) 
					{
						outFile.mkdirs();
					} 
					else 
					{	 	
						FileOutputStream fos = new FileOutputStream(outFile);
						dest = new BufferedOutputStream(fos, 102222);
						while ((count = is.read(data, 0, 102222)) != -1)
						{
							dest.write(data, 0, count);
						}
						dest.flush();
						dest.close();
						is.close();
					}
				}

				callbackContext.success("extract success!");
			} catch (ZipException evt) {
				// TODO Auto-generated catch block

				callbackContext.error("ZipException failed:"+evt);
			} catch (IOException evt) {
				// TODO Auto-generated catch block

				callbackContext.error("ZipException failed:"+evt);
			}



		}//end run

	}//end extract class


}


