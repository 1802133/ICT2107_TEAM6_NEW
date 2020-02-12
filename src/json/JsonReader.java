package json;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonReader {
	private static String filePath = "C::\\\\desktop\\\\anything.json";
	public static ArrayList<HashMap<String,?>> JsonRead(String grpConvo, String usrGrp, String grpUsr, String grpIP) {
		ArrayList<HashMap<String,?>> arrayList = new ArrayList<HashMap<String,?>>();
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		
		try (Reader reader = new FileReader(filePath)){
			Object object = parser.parse(reader);
			jsonArray = (JSONArray) object;
			jsonArray.forEach(array -> parseJsonObject(grpConvo, usrGrp, grpUsr, grpIP, (JSONObject) array, arrayList));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	private static void parseJsonObject(String grpConvo, String usrGrp, String grpUsr, String grpIP, JSONObject array, ArrayList<HashMap<String,?>> arrayList) {
		// TODO Auto-generated method stub
		//Array = usrGrp, grpUsr, etcetc
		arrayList.add(getHashList(grpConvo, array));
		arrayList.add(getHashList(grpUsr, array));
		arrayList.add(getHashList(usrGrp, array));
		arrayList.add(getHash(grpIP, array));
	}
	
	private static HashMap<String, HashMap<String, ArrayList<String>>> getHashList(String grpName, JSONObject array){
		JSONObject jsonObject = new JSONObject();
		if ((boolean) array.get(grpName)){//array based contains grpusr. grpIP...
			jsonObject = (JSONObject) array.get(grpName);
			HashMap<String, HashMap<String, ArrayList<String>>> hashMap = new HashMap<String, HashMap<String, ArrayList<String>>>();
			for (Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {//contains object (string, object)
				String key = (String) iterator.next();
				//find the list in key
				Object subObject = jsonObject.get(key);
				JSONArray subJsonArray = (JSONArray) subObject;
				HashMap<String, ArrayList<String>> subHash = new HashMap<String, ArrayList<String>>();
				for (int i = 0; i < subJsonArray.size(); i++) { // contains object(string, value))
					JSONObject jsonSubObject = (JSONObject) subJsonArray.get(i);
					for (Iterator subIterator = jsonSubObject.keySet().iterator(); subIterator.hasNext();) {
						String subKey = (String) subIterator.next();
						//loop through array might need to
						ArrayList<String> subValue = (ArrayList<String>) jsonSubObject.get(subKey);
						subHash.put(subKey, subValue);					
					}
				}
				hashMap.put(key,subHash);
			}
			return hashMap;
		}
		return null;
	}
	
	private static HashMap<String, String> getHash(String grpName, JSONObject array){
		JSONObject jsonObject = new JSONObject();
		HashMap<String, String> hashMap = new HashMap<String, String>();
		if ((boolean) array.get(grpName)){
			jsonObject = (JSONObject) array.get(grpName);
			for (Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				hashMap.put(key, (String) jsonObject.get(key));
			}
		}
		return hashMap;
	}
}
