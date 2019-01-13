package net.tommyc.android.tourofhonor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class jsonBonuses {

    private String bonusCode;
    private String category;
    private String name;
    private int value;
    private String address;
    private String city;
    private String state;
    private String GPS;
    private String access;
    private String flavor;
    private String madeInAmerica;
    private String imageName;

    public String getBonusCode() {
        return this.bonusCode;
    }

    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getGPS() {
        return this.GPS;
    }

    public String getAccess() {
        return this.access;
    }

    public String getFlavor() {
        return this.flavor;
    }

    public String getMadeInAmerica() {
        return this.madeInAmerica;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setBonusCode(String bonusCode) {
        this.bonusCode = bonusCode;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public void setMadeInAmerica(String madeInAmerica) {
        this.madeInAmerica = madeInAmerica;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    // Decodes bonus json into bonus model object
    public static jsonBonuses fromJson(JSONObject jsonObject) {
        jsonBonuses b = new jsonBonuses();
        // Deserialize json into object fields
        try {
            b.bonusCode = jsonObject.getString("bonusCode");
            b.category = jsonObject.getString("category");
            b.name = jsonObject.getString("name");
            b.value = jsonObject.getInt("value");
            b.address = jsonObject.getString("address");
            b.city = jsonObject.getString("city");
            b.state = jsonObject.getString("state");
            b.GPS = jsonObject.getString("GPS");
            b.access = jsonObject.getString("Access");
            b.flavor = jsonObject.getString("flavor");
            b.madeInAmerica = jsonObject.getString("madeinamerica");
            b.imageName = jsonObject.getString("imageName");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }

    // Decodes array of bonus json results into bonus model objects
    public static ArrayList<jsonBonuses> fromJson(JSONArray jsonArray) {
        JSONObject jsonObject;
        ArrayList<jsonBonuses> bonuses = new ArrayList<jsonBonuses>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            jsonBonuses bonus = jsonBonuses.fromJson(jsonObject);
            if (bonus != null) {
                bonuses.add(bonus);
            }
        }

        return bonuses;
    }
}
