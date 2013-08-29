/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;


public class JsonTools {




   @SuppressWarnings("unchecked")
public JSONObject addArgument(JSONObject obj, String value, String propertyID) throws IOException 
   {
      
      obj.put(propertyID,value);
      
	return obj;
   }
}
