package com.velisphere.toucan.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;





public class DownloadServlet extends HttpServlet{
	          
	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	       // String encodedFileName = req.getRequestURI().substring(req.getContextPath().length() + req.getServletPath().length() + 1);
	        //String decodedFileName = URLDecoder.decode(encodedFileName, "utf-8");    

	        
	    	
	    	
	        File downloadableFile = new File(req.getParameter("privateURL"));
	        
	        resp.setContentType("text/csv");
	        resp.setHeader("Content-Disposition", "attachment;filename=\""+req.getParameter("outboundFileName"));
	        
	        ServletOutputStream os = resp.getOutputStream();
	   
	        try {
	            InputStream is = FileUtils.openInputStream(downloadableFile);
	            try {
	                IOUtils.copy(is, os);
	            } finally {
	                is.close();
	                
	            }
	        } finally {
	        	os.close();
	        }
	        
	        String persist = req.getParameter("persist");
	        if (persist.equals("1") == false){
	        	downloadableFile.delete();	
	        }
	        

    }
}