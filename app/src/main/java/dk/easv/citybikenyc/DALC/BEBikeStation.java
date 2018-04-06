package dk.easv.citybikenyc.DALC;


import org.json.JSONException;
import org.json.JSONObject;

public class BEBikeStation {

	private int m_number;
	private String m_name;

	public BEBikeStation(int number, String name)
	{
		setNumber(number);
		setName(name);
	}

	public BEBikeStation(JSONObject object) throws JSONException {
		setNumber(Integer.parseInt(object.getString("id")));
		setName(object.getString("stationName"));
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}

	public String getName() {
		return m_name;
	}

	public void setNumber(int number) {
		this.m_number = number;
	}

	public int getNumber() { return m_number; }

}
