<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<title>Consumption page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="resources/theme/css/loginStyle.css"
	type="text/css">
<!--Load the AJAX API-->
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">

      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart','controls']});
      google.charts.setOnLoadCallback(drawStuff);

      var averageDay = ${averageDay};
      var averageWeek = ${averageWeek};
      var averageMonth = ${averageMonth};
      var averageNeighD = ${averageNeighD};
      var averageNeighW = ${averageNeighW};
      var averageNeighM = ${averageNeighM};
      //Definition of the array needed for the chart.
      var dataArrayDay = [["Days", "Consumption", "Average", "AverageNeigh"]];
      var dataArrayWeek = [["Days", "Consumption", "Average", "AverageNeigh"]];
	  var dataArrayMonth = [["Days", "Consumption", "Average", "AverageNeigh"]];

	  //This cycle is used to fill the array of the day consumption.
      <c:forEach var="day" items="${DailyData}" varStatus="loop">
		var date =  new Date('${day.period}');
		var consumption = ${day.consumption};
		dataArrayDay.push([date,parseFloat(consumption),averageDay,averageNeighD]);
      </c:forEach>

	  //This cycle is used to fill the array of the week consumption.
      <c:forEach var="week" items="${WeeklyData}" varStatus="loop">
		var date =  new Date('${week.period}');
		var consumption = ${week.consumption};
		dataArrayWeek.push([date,parseFloat(consumption),averageWeek,averageNeighW]);
      </c:forEach>

	  //This cycle is used to fill the array of the month consumption.
      <c:forEach var="month" items="${MonthlyData}" varStatus="loop">
		var date = new Date('${month.period}');
		var consumption = ${month.consumption};
		dataArrayMonth.push([date,parseFloat(consumption),averageMonth,averageNeighM]);
      </c:forEach>
      

      //Function used to draw the various charts.
      function drawStuff() {
        var dataDay = new google.visualization.arrayToDataTable(dataArrayDay);
        var dataWeek = new google.visualization.arrayToDataTable(dataArrayWeek);
        var dataMonth = new google.visualization.arrayToDataTable(dataArrayMonth);
        var dashboard = new google.visualization.Dashboard(document.getElementById('programmatic_dashboard_div'));

        // We omit "var" so that programmaticSlider is visible to changeRange.
                programmaticSlider = new google.visualization.ControlWrapper({
                  'controlType': 'ChartRangeFilter',
                  'containerId': 'programmatic_control_div',
                  'options': {
                    'filterColumnLabel': 'Days',
                    'ui': {
                        'chartType': 'ComboChart',
                            'chartArea': {'height': '100%', 'width': '100%'},
                  'chartOptions': {
                              'hAxis': {'position': 'none'},
                                'vAxis': {
                                  'textPosition': 'none',
                                    'gridLines': {'color': 'none'}
                                 }
                            }
                           }
                  }
                });


                programmaticChart  = new google.visualization.ChartWrapper({
                    'chartType': 'ComboChart',
                    'containerId': 'programmatic_chart_div',
                    'options': {
                      'legend': 'right',
                      'vAxis': {'title': 'WATER CONSUMPTION(m³)'},
                      'hAxis': {'title': 'Days', 'type': 'date'},
                      'seriesType': 'bars',
                     },
                      'view': {columns: [0, 1]}
                  });

                programmaticChartAverageNeighborhood  = new google.visualization.ChartWrapper({
                    'chartType': 'ComboChart',
                    'containerId': 'programmatic_chart_div',
                    'options': {
                      'legend': 'right',
                      'vAxis': {'title': 'WATER CONSUMPTION(m³)'},
                      'hAxis': {'title': 'Days', 'type': 'date'},
                      'seriesType': 'bars',
                      'series': {1: {'type': 'line'}, 2: {'type': 'line'}},
                     },
                      'view': {columns: [0, 1, 3]}
                  });
                
                  programmaticChartAverage  = new google.visualization.ChartWrapper({
                    'chartType': 'ComboChart',
                    'containerId': 'programmatic_chart_div',
                    'options': {
                      'legend': 'right',
                      'vAxis': {'title': 'WATER CONSUMPTION(m³)'},
                      'hAxis': {'title': 'Days', 'type': 'date'},
                      'seriesType': 'bars',
                      'series': {1: {'type': 'line'}, 2: {'type': 'line'}},
                     },
                      'view': {columns: [0, 1, 2]}
                  });

                  programmaticChartAll  = new google.visualization.ChartWrapper({
                      'chartType': 'ComboChart',
                      'containerId': 'programmatic_chart_div',
                      'options': {
                        'legend': 'right',
                        'vAxis': {'title': 'WATER CONSUMPTION(m³)'},
                        'hAxis': {'title': 'Days', 'type': 'date'},
                        'seriesType': 'bars',
                        'series': {1: {'type': 'line'}, 2: {'type': 'line'}},
                       },
                        'view': {columns: [0, 1, 2, 3]}
                    });
                  
            	  var checkboxAverage = document.getElementById("showAverage");
            	  var averageControl = false;
            	  //Function that manage the onClick event on the button to show 
            	  //the  daily, weekly and monthly consumption average of the logged user. 
            	  checkboxAverage.onclick = function()
                  {
                    if(averageControl){
                    
                  	    dashboard.bind(programmaticSlider, programmaticChartAll);
                        dashboard.draw(dataDay);
                        checkboxAverage.disabled = true;
                        }
                    else{
                   
                        dashboard.bind(programmaticSlider, programmaticChartAverage);
                        dashboard.draw(dataDay);
                        checkboxAverage.disabled = true;
                        averageControl = true ;
                        }
                	
                  }
            	  //Function that manage the onClick event on the button to show 
            	  //the  daily, weekly and monthly consumption average of the neighbour of the logged user.
            	  var showAverageNeighborhood = document.getElementById("showAverageNeighborhood");
                  showAverageNeighborhood.onclick = function()
                  {
                      if(averageControl){
                    	
                    	  dashboard.bind(programmaticSlider, programmaticChartAll);
                          dashboard.draw(dataDay);
                          showAverageNeighborhood.disabled = true;
                          }
                      else{
                    	 
                          dashboard.bind(programmaticSlider, programmaticChartAverageNeighborhood);
                          dashboard.draw(dataDay);
                          showAverageNeighborhood.disabled = true;
                          averageControl = true ;
                          }
                  }

              
				  //These three function manage the chart of the
				  //monthly, weekly and daily consumption data, respectively.
                  var buttonMonth = document.getElementById("buttonMonth");
                  buttonMonth.onclick = function()
                  {
                  dashboard.bind(programmaticSlider, programmaticChart);
                     dashboard.draw(dataMonth);
                  }

                  var buttonDay = document.getElementById("buttonWeek");
                  buttonDay.onclick = function()
                  {
                  dashboard.bind(programmaticSlider, programmaticChart);
                     dashboard.draw(dataWeek);
                  }

                  var buttonDay = document.getElementById("buttonDay");
                  buttonDay.onclick = function()
                  {
                  dashboard.bind(programmaticSlider, programmaticChart);
                     dashboard.draw(dataDay);
                  }
                  
                  dashboard.bind(programmaticSlider, programmaticChart);
                  dashboard.draw(dataDay);

                  
  }
      
      </script>
</head>
<body>
<p style="font-size:35px; margin-top:0px" align="left" >WATER CONSUMPTION PROJECT</p>
<p style="font-size:15px" align="right" >You are logged-in as ${user.username}</p>

		<div class="granularity"><b>Change Granularity: </b>
		<button type="button" id="buttonDay">DayView</button>
		<button type="button" id="buttonWeek" class="buttonWeek">WeekView</button>
		<button type="button" id="buttonMonth">MonthView</button>
		</div>
		<div class="average"><b>Add Average: </b>
		<button name="html" value="AverageNeigh"
			id="showAverageNeighborhood" >Average Neigh</button>
		<button name="html" value="Average" 
			id="showAverage" >Average</button><br /> 
		</div>
		<br/>
	

<form method="post">

		<spring:bind path="user.username">
			<input type="hidden" 
				 name="${status.expression}" value="${user.username}">
		</spring:bind>
		<spring:bind path="user.oid">
			<input type="hidden" 
				 name="${status.expression}" value="${user.oid}">
		</spring:bind>
		
		<input type="submit" value="Map View">
</form>
<br/>

		<div id="programmatic_dashboard_div" >

		<div id="programmatic_chart_div" style="height:300px"></div>

		<div id="programmatic_control_div" style="height:100px"></div>

	</div>
</body>
</html>