/**
 * 
 */
package polimi.awt.wcp.model;

import java.io.Serializable;
import javax.persistence.*;

import polimi.awt.wcp.model.Household;

/**
 * @author Jacopo Magni
 *
 * The persistent class for the neutral_user database table.
 *
 */
@Entity
@Table(name="neutral_user")
@NamedQuery(name="NeutralUser.findAll", query="SELECT n FROM NeutralUser n")
public class NeutralUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_oid", insertable = false, updatable = false)
	private int userOid;
	
	@Column(name="public")
	private int okPublic;
	

	//bi-directional many-to-one association to Household
	@ManyToOne
	private Household household;

	//-------- Constructor --------//
	public NeutralUser() {
	}
	
	//-------- Getter and Setters --------//
	public int getUserOid() {
		return this.userOid;
	}

	public void setUserOid(int userOid) {
		this.userOid = userOid;
	}
	
	public Household getHousehold() {
		return this.household;
	}

	public void setHousehold(Household household) {
		this.household = household;
	}
	
	public int getOkPublic() {
		return okPublic;
	}

	public void setOkPublic(int okPublic) {
		this.okPublic = okPublic;
	}
}
