package polimi.awt.wcp.model;

import java.math.BigDecimal;

/**
 * @author rezzo
 * 
 * The class created to manage user elaborated data.
 * 
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author rezzo
 * 
 * The class created to manage the elaborated data of the consumption. 
 * It stores together the period of time and the water consumption value for that particular period.
 * 
 */
public class ElaboratedConsumptionData {

	private Date period;
	private BigDecimal consumption;

	//-------- Getter and Setters --------//
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	public BigDecimal getConsumption() {
		return consumption;
	}
	public void setConsumption(BigDecimal consumption) {
		this.consumption = consumption;
	}
	
	//Method to convert a DateFormat to String 
	public String DateToString(Date datePeriod){

		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String date = df.format(datePeriod);
		return date;
	}

}
