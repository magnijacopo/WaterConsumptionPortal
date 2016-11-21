/**
 * 
 */
package polimi.awt.wcp.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Jacopo Magni
 *
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int oid;
	
	@NotBlank(message = "{NotBlank.user.password}")
	@Size(min=8, max=30)
	private String password;

	private String username;
	
	//bi-directional one-to-one association to NeutralUser
	@OneToOne
	@JoinColumn(name="oid")
	private NeutralUser neutralUser;
	
	//-------- Constructor --------//
	public User() {
	}
	
	//-------- Getter and Setters --------//
	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public NeutralUser getNeutralUser() {
		return neutralUser;
	}

	public void setNeutralUser(NeutralUser neutralUser) {
		this.neutralUser = neutralUser;
	}

	
}
