/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
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
package com.velisphere.tigerspice.client.helper;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {

	public static String randomColor()  
    {  
        String[] letters = new String[15];  
        letters = "0123456789ABCDEF".split("");  
        String code ="#";  
        for(int i=0;i<6;i++)  
        {  
            double ind = Math.random() * 15;  
            int index = (int)Math.round(ind);  
            code += letters[index];   
        }  
        return code;  
    }  
	
}
