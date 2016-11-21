package polimi.awt.wcp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * @author rezzo
 * 
 * The persistent class for the district database table.
 * 
 */
@Entity
@NamedQuery(name="District.findAll", query="SELECT d FROM District d")
public class District implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int oid;

	private String city;

	private String country;

	private String name;

	private String zipcode;

	//bi-directional many-to-one association to Building
	@OneToMany(mappedBy="district")
	private List<Building> buildings;

	//-------- Constructor --------//
	public District() {
	}

	//-------- Getter and Setters --------//
	public int getOid() {
		return this.oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public List<Building> getBuildings() {
		return this.buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	public Building addBuilding(Building building) {
		getBuildings().add(building);
		building.setDistrict(this);

		return building;
	}

	public Building removeBuilding(Building building) {
		getBuildings().remove(building);
		building.setDistrict(null);

		return building;
	}

}