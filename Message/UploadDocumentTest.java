/*
 * $Id: UploadDocumentTest.java,v 1.1 2007/05/22 14:40:11 dorelv Exp $
 *
 * This unpublished source code contains trade secrets and copyrighted
 * materials that are the proprietary property of iseemedia, Inc.
 * Unauthorized use, copying or distribution of this source code or the
 * ideas contained herein is a violation of U.S. and international laws
 * and is strictly prohibited.
 *
 * Copyright (c) 2002,2003,2004 iseemedia, Inc. All Rights Reserved.
 *
 */

package com.iseemedia.util.test;

/**
 * @version $Revision: 1.1 $
 * @author dvleju
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import junit.framework.TestCase;


/**
 * @author dvleju
 *
 * Test class for uploading a document to iseedocs server
 */
public class UploadDocumentTest extends TestCase {
	

	private static final boolean GET_IDX = true;
	private static final boolean FULLY_ADAPT = true;
	
	private static final String FILE_NAME = "tivoli_messageguide.pdf";
	
	private static final int POSTINGS = 20;

	public void testPosting() {
		for (int i = 1; i <= POSTINGS; i++){
			System.out.println(i + ". \tPOSTING file: " + FILE_NAME + " \tGET_IDX=" + GET_IDX + " \tFULLY_ADAPT=" + FULLY_ADAPT);
			postDocument(i);
		}
	}
	
	public void postDocument(int id) {

		
		try {
			long start = System.currentTimeMillis();
			File file = new File(FILE_NAME);
			
			InputStream in = new FileInputStream(file);
			
			String adaptFully = "" + FULLY_ADAPT;
			String fragments = "link";
			if (GET_IDX)
				fragments = "idx";
			
			// create URL
		    URL url = new URL("http://localhost/iseepdf/adapt?po_id=" + id + "&fragments=" + fragments + "&adaptFully=" + adaptFully + "&file=" + FILE_NAME);
	      
		    // Open the connection and prepare to POST
		    HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		    uc.setDoOutput(true);
		    uc.setDoInput(true);
		    uc.setAllowUserInteraction(false);
		    uc.setRequestProperty("Content-Length", "" + file.length());
		    uc.setRequestProperty("Content-Type", "application/stream");
	
		    byte [] buf = new byte[4096];
			
			OutputStream out = uc.getOutputStream();
			
			int length = 0;
			int count = 0;
			
			// write file on Connection input stream
			while (length > -1){
				length = in.read(buf);
				
				if (length > 0){
					out.write(buf, 0, length);
					count += length;
				}
			}
			
			in.close();
			out.close();
			//System.out.println("Posted " + count + " bytes.");
			
		    // Read Response
		    InputStream response = uc.getInputStream();
		    int x;
		    while ( (x = response.read()) != -1)
		    {
		    	System.out.write(x);
		    }
		    System.out.println();
		    
		    response.close();

		    System.out.println("\tDone in " + (System.currentTimeMillis() - start) + " millis.");
			
		}catch (Exception e) {
			System.out.println("Uploading file: Failed Message:\n"
					+ e.getMessage());
			e.printStackTrace();
			
		}
	}

}