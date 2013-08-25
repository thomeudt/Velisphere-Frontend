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
