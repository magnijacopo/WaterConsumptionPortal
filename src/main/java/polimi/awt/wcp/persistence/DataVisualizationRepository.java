package polimi.awt.wcp.persistence;

import java.util.List;

import polimi.awt.wcp.model.MeterReading;
import polimi.awt.wcp.model.User;

public interface DataVisualizationRepository {

	public List<MeterReading> ConsumptionData(User user);
	public List<User> neighbourList(User user);
	
}
