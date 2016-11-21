package polimi.awt.wcp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * @author rezzo
 * 
 * The persistent class for the household database table.
 * 
 */
@Entity
@NamedQuery(name="Household.findAll", query="SELECT h FROM Household h")
public class Household implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int oid;

	@Column(name="family_id")
	private String familyId;

	//bi-directional many-to-one association to Building
	@ManyToOne
	private Building building;

	//bi-directional many-to-one association to SmartMeter
	@ManyToOne
	@JoinColumn(name="smart_meter_oid")
	private SmartMeter smartMeter;

	//bi-directional one-to-many association to NeutralUser
	@OneToMany(mappedBy="household")
	private List<NeutralUser> neutralUsers;

	public Household() {
	}

	public int getOid() {
		return this.oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getFamilyId() {
		return this.familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public SmartMeter getSmartMeter() {
		return this.smartMeter;
	}

	public void setSmartMeter(SmartMeter smartMeter) {
		this.smartMeter = smartMeter;
	}

	public List<NeutralUser> getNeutralUsers() {
		return this.neutralUsers;
	}

	public void setNeutralUsers(List<NeutralUser> neutralUsers) {
		this.neutralUsers = neutralUsers;
	}

	public NeutralUser addNeutralUser(NeutralUser neutralUser) {
		getNeutralUsers().add(neutralUser);
		neutralUser.setHousehold(this);

		return neutralUser;
	}

	public NeutralUser removeNeutralUser(NeutralUser neutralUser) {
		getNeutralUsers().remove(neutralUser);
		neutralUser.setHousehold(null);

		return neutralUser;
	}

}