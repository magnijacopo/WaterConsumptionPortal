package polimi.awt.wcp.model;

/**
 * @author rezzo
 * 
 * The class is used to manage the user data needed for the pop up in the map page.
 * 
 */
public class ElaboratedUserData {

	private String country;
	private String city;
	private String street;
	private String name;
	private String zipCode;
	private boolean individual;
	private String username;
	private float averageDay;
	private float averageWeek;
	private float averageMonth;
	private float lastReading;
	
	//-------- Getter and Setters --------//
	public float getLastReading() {
		return lastReading;
	}
	public void setLastReading(float lastReading) {
		this.lastReading = lastReading;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIndividual() {
		return individual;
	}
	public void setIndividual(boolean individual) {
		this.individual = individual;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public float getAverageDay() {
		return averageDay;
	}
	public void setAverageDay(float averageDay) {
		this.averageDay = averageDay;
	}
	public float getAverageWeek() {
		return averageWeek;
	}
	public void setAverageWeek(float averageWeek) {
		this.averageWeek = averageWeek;
	}
	public float getAverageMonth() {
		return averageMonth;
	}
	public void setAverageMonth(float averageMonth) {
		this.averageMonth = averageMonth;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
