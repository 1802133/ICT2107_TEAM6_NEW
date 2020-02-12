package json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.*;


public class json {
private static final String filePath = "UserStorage.json";
private static final String fileName = "userStorage";
	
	public static void JsonWriterController(String grpConvo, HashMap<String, List<String>> grpConvoHash, String usrGrp, HashMap<String, List<String>> usrGrpHash, String grpUsr, HashMap<String, List<String>> grpUsrHash, String grpIP, HashMap<String, String> grpIPHash, String usrProfile, HashMap<String, List<String>> usrProfileHash) throws IOException {
		JsonClearFile();
//		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//Error occurs at write after parsing in new values
		jsonArray.add(JsonWriterList(grpConvo, grpConvoHash));
		jsonArray.add(JsonWriterList(usrGrp, usrGrpHash));
		jsonArray.add(JsonWriterList(grpUsr, grpUsrHash));
		jsonArray.add(JsonWriter(grpIP, grpIPHash));
		jsonArray.add(JsonWriterList(usrProfile, usrProfileHash));
//		jsonObject.put(fileName, jsonArray);
		JsonWrite(jsonArray);
		
	}
	
	public static ArrayList<HashMap<String,?>> JsonRead(String grpConvo, String usrGrp, String grpUsr, String grpIP, String usrProfile) {
		ArrayList<HashMap<String,?>> arrayList = new ArrayList<HashMap<String,?>>();
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		
		File temp = new File(filePath);
		if (temp.exists()) {
			try (Reader reader = new FileReader(filePath)){
				jsonArray = (JSONArray) parser.parse(reader);
				jsonArray.forEach(array -> parseJsonObject(grpConvo, usrGrp, grpUsr, grpIP, usrProfile, (JSONObject) array, arrayList));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return arrayList;
	}
	
	private static JSONObject JsonWriterList(String objectName, HashMap<String, List<String>> hashMap) {
		JSONObject obj = new JSONObject();
		JSONObject subObj = new JSONObject();
		for (Map.Entry entries: hashMap.entrySet()) {
			JsonArray valueArray = new Gson().toJsonTree(entries.getValue()).getAsJsonArray();
			subObj.put(entries.getKey().toString(), valueArray);
		}
		obj.put(objectName, subObj);
		
		return obj;
	}
	
	private static JSONObject JsonWriter(String objectName, HashMap<String, String> hashMap) {
		JSONObject obj = new JSONObject();
		JSONObject subObj = new JSONObject();
		for (Map.Entry entries: hashMap.entrySet()) {
			subObj.put(entries.getKey(), entries.getValue());
		}
		obj.put(objectName, subObj);
		return obj;
	}
	
	private static void JsonClearFile() {
		try (FileWriter file = new FileWriter(filePath, false)){
			file.write("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void JsonWrite(JSONArray jsonArray) {
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
	private static void parseJsonObject(String grpConvo, String usrGrp, String grpUsr, String grpIP, String usrProfile, JSONObject array, ArrayList<HashMap<String,?>> arrayList) {
		// TODO Auto-generated method stub
		//Array = usrGrp, grpUsr, etcetc
		if (array.containsKey(grpConvo)) {
			arrayList.add(getHashList(grpConvo, array));
		}
		if (array.containsKey(grpUsr)) {
			arrayList.add(getHashList(grpUsr, array));
		}
		if (array.containsKey(usrGrp)) {
			arrayList.add(getHashList(usrGrp, array));
		}
		if (array.containsKey(grpIP)) {
			arrayList.add(getHash(grpIP, array));
		}
		if (array.containsKey(usrProfile)) {
			arrayList.add(getHashList(usrProfile, array));
		}

	}
	
	private static HashMap<String, HashMap<String, List<String>>> getHashList(String grpName, JSONObject array){
		JSONObject jsonObject = new JSONObject();
		HashMap<String, HashMap<String, List<String>>> hashMap = new HashMap<String, HashMap<String, List<String>>>();
		if (array.get(grpName) != null){//array based contains grpusr. grpIP...
			jsonObject = (JSONObject) array.get(grpName);
			HashMap<String, List<String>> subHash = new HashMap<String, List<String>>();
			for (Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {//contains object (string, object)
				String key = (String) iterator.next();
				//find the list in key
				Object subObject = jsonObject.get(key);
				JSONArray subJsonArray = (JSONArray) subObject;
				subHash.put(key,subJsonArray);
			}
			hashMap.put(grpName, subHash);
			return hashMap;
		}
		return null;
	}
	
	private static HashMap<String, HashMap<String, String>> getHash(String grpName, JSONObject array){
		JSONObject jsonObject = new JSONObject();
		HashMap<String, HashMap<String, String>> hashMap = new HashMap<String, HashMap<String, String>>();
		if (array.get(grpName) != null){
			jsonObject = (JSONObject) array.get(grpName);
			HashMap<String, String> subHash = new HashMap<String, String>();
			for (Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				subHash.put(key, (String) jsonObject.get(key));
			}
			hashMap.put(grpName, subHash);
			return hashMap;
		}
		return null;
	}
}
