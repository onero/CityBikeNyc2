package dk.easv.citybikenyc.DALC;


import org.json.JSONException;
import org.json.JSONObject;

public class BEBikeStation {

	private int m_number;
	private String m_name;
	private String mStatus;

	public BEBikeStation(int number, String name)
	{
		setNumber(number);
		setName(name);
	}

	/***
	 * Create BikeStation from JSON object
	 * @param object
	 * @throws JSONException
	 */
	public BEBikeStation(JSONObject object) throws JSONException {
		setNumber(Integer.parseInt(object.getString("id")));
		setName(object.getString("stationName"));
		setStatus(object.getString("statusValue"));
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

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		mStatus = status;
	}
}
