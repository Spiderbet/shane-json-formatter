import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONFormatter {

	public static void main(String[] args) {

		//Need to pass in the folder of JSON files as an argument
		String folderPath = args[0];

		File folder = new File(folderPath);

		if (!folder.isDirectory()) {
			System.out.println("ERROR: Path is not a folder!");
			System.exit(1);
		}

		File newFolder = new File(folder + File.separator + "output");
		newFolder.delete();
		newFolder.mkdirs();

		for (String fileName : folder.list()) {

			File file = new File(folder + File.separator + fileName);

			if (file.isFile()) {
				try {
					Scanner scan = new Scanner(file);

					String textContents = "";

					while (scan.hasNext()) {
						textContents += scan.next();
					}

					JSONObject newPropertiesObject = convertToNewJSONObject(textContents, fileName);

					JSONObject newObject = new JSONObject();
					newObject.put("standard", "arc69");
					newObject.put("description", JSONObject.NULL);
					newObject.put("external_url", JSONObject.NULL);
					newObject.put("mime_type", "image/jpeg");

					newObject.put("properties", newPropertiesObject);

					System.out.println("INFO: Writing file: " + newFolder + File.separator + fileName);
					try {
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(newFolder + File.separator + fileName));
						writer.write(newObject.toString(4));

						writer.close();
					} catch (Exception ex) {
						System.out.println("ERROR: Failed to write file: " + newFolder + File.separator + fileName);
					}

					scan.close();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private static JSONObject convertToNewJSONObject(String textContents, String fileName) {

		JSONObject object = new JSONObject(textContents);
		JSONObject newObject = new JSONObject();
		JSONArray newPropertiesArray = new JSONArray();

		if (!object.has("properties")) {
			System.out.println("ERROR: File does not have a properties key: " + fileName);
		} else {

			JSONArray propertiesArray = object.getJSONArray("properties");

			for (int i = 0; i < propertiesArray.length(); i++) {

				JSONObject propertyObject = propertiesArray.getJSONObject(i);

				String trait = propertyObject.getString("trait");
				String value = propertyObject.getString("value");

//				JSONObject newPropertyObject = new JSONObject();
//				newPropertyObject.put(trait, value);
				
				newPropertiesArray.put("\"" + trait + ":\" " + "\"" + value + "\"");
				newObject.put(trait, value);

//				newPropertiesArray.put(newPropertyObject);

			}

		}

		return newObject;
	}

}
