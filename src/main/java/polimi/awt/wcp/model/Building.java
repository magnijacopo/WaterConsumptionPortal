package polimi.awt.wcp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * @author rezzo
 * 
 * The persistent class for the building database table.
 * 
 */
@Entity
@NamedQuery(name="Building.findAll", query="SELECT b FROM Building b")
public class Building implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int oid;

	private String address;

	//bi-directional many-to-one association to District
	@ManyToOne
	private District district;

	//bi-directional one-to-many association to Household
	@OneToMany(mappedBy="building")
	private List<Household> households;

	//bi-directional one-to-many association to SmartMeter
	@OneToMany(mappedBy="building")
	private List<SmartMeter> smartMeters;

	//-------- Constructor --------//
	public Building() {
	}
	
	//-------- Getter and Setters --------//
	public int getOid() {
		return this.oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public District getDistrict() {
		return this.district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public List<Household> getHouseholds() {
		return this.households;
	}

	public void setHouseholds(List<Household> households) {
		this.households = households;
	}

	public Household addHousehold(Household household) {
		getHouseholds().add(household);
		household.setBuilding(this);

		return household;
	}

	public Household removeHousehold(Household household) {
		getHouseholds().remove(household);
		household.setBuilding(null);

		return household;
	}

	public List<SmartMeter> getSmartMeters() {
		return this.smartMeters;
	}

	public void setSmartMeters(List<SmartMeter> smartMeters) {
		this.smartMeters = smartMeters;
	}

	public SmartMeter addSmartMeter(SmartMeter smartMeter) {
		getSmartMeters().add(smartMeter);
		smartMeter.setBuilding(this);

		return smartMeter;
	}

	public SmartMeter removeSmartMeter(SmartMeter smartMeter) {
		getSmartMeters().remove(smartMeter);
		smartMeter.setBuilding(null);

		return smartMeter;
	}

}