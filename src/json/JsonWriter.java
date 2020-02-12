package json;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonWriter {
	private static final String filePath = "C::\\desktop\\anything.json";
	
	public static void JsonWriterController(String grpConvo, HashMap<String, ArrayList<String>> grpConvoHash, String usrGrp, HashMap<String, ArrayList<String>> usrGrpHash, String grpUsr, HashMap<String, ArrayList<String>> grpUsrHash, String grpIP, HashMap<String, String> grpIPHash) throws IOException {
		JsonClearFile();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(JsonWriterList(grpConvo, grpConvoHash));
		jsonArray.add(JsonWriterList(usrGrp, usrGrpHash));
		jsonArray.add(JsonWriterList(grpUsr, grpUsrHash));
		jsonArray.add(JsonWriter(grpIP, grpIPHash));
		JsonWrite(jsonArray);
	}
	
	public static JSONObject JsonWriterList(String objectName, HashMap<String, ArrayList<String>> hashMap) {
		JSONObject obj = new JSONObject();
		JSONObject subObj = new JSONObject();
		for (Map.Entry entries: hashMap.entrySet()) {
			JSONArray valueArray = (JSONArray) entries.getValue();
			subObj.put(entries.getKey().toString(), valueArray);
		}
		obj.put(objectName, subObj);
		
		return obj;
	}
	
	public static JSONObject JsonWriter(String objectName, HashMap<String, String> hashMap) {
		JSONObject obj = new JSONObject();
		for (Map.Entry entries: hashMap.entrySet()) {
			obj.put(entries.getKey(), entries.getValue());
		}
		return obj;
	}
	
	public static void JsonClearFile() {
		try (FileWriter file = new FileWriter(filePath, false)){
			file.write("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void JsonWrite(JSONArray jsonArray) {
		try (FileWriter file = new FileWriter(filePath, true)){
			file.write(jsonArray.toJSONString());
			file.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//output to terminal
		System.out.print(jsonArray);
	}
}
