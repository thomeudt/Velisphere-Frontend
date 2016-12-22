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
