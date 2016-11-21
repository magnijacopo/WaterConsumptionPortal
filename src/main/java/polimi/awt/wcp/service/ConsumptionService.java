package polimi.awt.wcp.service;

import java.util.List;

import polimi.awt.wcp.model.ElaboratedConsumptionData;
import polimi.awt.wcp.model.User;

public interface ConsumptionService {

	public List<ElaboratedConsumptionData> DayVisualization(User user);
	public List<ElaboratedConsumptionData> WeekVisualization(User user);
	public List<ElaboratedConsumptionData> MonthVisualization(User user);
	public float calculateAverage(List<ElaboratedConsumptionData> listEcd);
	public List<Float> calculateAvgNeighbourhood(User user);
	

}