<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/theme/css/loginStyle.css"
	type="text/css">
<style>
#map {
	width: 100%;
	height: 400px;
}
</style>

</head>
<body>
	<p style="font-size:35px; margin-top:0px" align="left" >WATER CONSUMPTION PROJECT</p>
	<p style="font-size:15px" align="right" >You are logged-in as ${user.username}</p>
	<div id="map"></div>
	<script type="text/javascript"
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js"></script>

	<script type="text/javascript">
	
		//var showUsersData = document.getElementById("showUsersData");
		var mapDiv;
		var map;
		var lab;
		var loggedUserIsPublic = false;
		var postalcode;
		var postalCodeCheck;

		//This function retrieve the JSON file from the geocoding API 
		//and then parse it, to find the zipcode.
		function myFunction() {
			var country = document.getElementById("country").value;
			var city = document.getElementById("city").value;
			var street = document.getElementById("street").value;
			var houseNum = document.getElementById("houseNum").value;
			var address1;
			

			// The Google Geocoding API url used to get the JSON data
			var geocodingAPI = "http://maps.googleapis.com/maps/api/geocode/json?address="+ houseNum+ "+"+street+",+"+ city+ ",+"+ country+ "&sensor=true";
			$.ajax({
			    async: false,
			    url: geocodingAPI,
			    success: function(json) {
			    	address1 = json.results[0].address_components;
					postalcode = json.results[0].address_components[address1.length - 1].long_name;
					document.getElementById("zipCode").value = postalcode;
					postalCodeCheck = postalcode;
			    }
			});

			// Caching the link jquery object
			var $myLink = $('a.myLink');

			// Set the links properties
			$myLink.prop({
				href : geocodingAPI,
				title : 'Click on this link to open in a new window.'
			}).click(function(e) {
				e.preventDefault();
				window.open(this.href, '_blank');
			});

		}

		//Add marker of the logged user to the map.
		function addLoggedMarker() {
			var country = '${loggedUser.country}';
			var city = '${loggedUser.city}';
			var street = String('${loggedUser.street}');
			var geocodingAPI = "http://maps.googleapis.com/maps/api/geocode/json?address="
					+ street + ",+" + city + ",+" + country + "&sensor=true";
			$.getJSON(geocodingAPI,function(json) {
								latitude = json.results[0].geometry.location.lat;
								longitude = json.results[0].geometry.location.lng;

								var lab;

								if ('${loggedUser.individual}' == true) {
									lab = 'i';
								} else {
									lab = 'c';
								}
								var contentStringLogged = '<div id="content">'
										+ '<h1>${loggedUser.username}</h1>'
										+ '<div>'
										+ '<table border="1">'
										+ '<tr><td>Current total:</td><td>${loggedUser.lastReading}</td></tr>'
										+ '<tr><td>Daily average:</td><td>${loggedUser.averageDay}</td></tr>'
										+ '<tr><td>Weekly average:</td><td>${loggedUser.averageWeek}</td></tr>'
										+ '<tr><td>Monthly average:</td><td>${loggedUser.averageMonth}</td></tr>'
										+ '</table>' + '</div>' + '</div>';

								var infowindowlogged = new google.maps.InfoWindow(
										{
											content : contentStringLogged
										});
								var marker = new google.maps.Marker({
									position : {
										lat : latitude,
										lng : longitude
									},
									label : lab,
									map : map
								});
								marker.addListener('click', function() {
									infowindowlogged.open(map, marker);
								});
							});
		}

		// Adds a marker to the map.
		function addMarker(latitude, longitude, map, user) {
			// Add the marker at the clicked location, and add the next-available label
			// from the array of alphabetical characters.
			var lab;
			var contentString;
			var pUser = "Null";
			var publicLastReading = "Null";
			var publicAverageDay = "Null";
			var publicAverageWeek = "Null";
			var publicAverageMonth = "Null";

			<c:forEach items="${publicUserData}" var="Rio2016">
			if ('${Rio2016.individual}' == true) {
				lab = 'i';
			} else {
				lab = 'c';
			}
			if('${Rio2016.username}' == user) {
				pUser = '${Rio2016.username}';
				publicLastReading = '${Rio2016.lastReading}';
				publicAverageDay = '${Rio2016.averageDay}';
				publicAverageWeek = '${Rio2016.averageWeek}';
				publicAverageMonth = '${Rio2016.averageMonth}';
			} 
			</c:forEach>
			contentString = '<div id="content">'
				+ '<h1>'+pUser+'</h1>'
				+ '<div>'
				+ '<table border="1">'
				+ '<tr><td>Current total:</td><td>'+publicLastReading+'</td></tr>'
				+ '<tr><td>Daily average:</td><td>'+publicAverageDay+'</td></tr>'
				+ '<tr><td>Weekly average:</td><td>'+publicAverageWeek+'</td></tr>'
				+ '<tr><td>Monthly average:</td><td>'+publicAverageMonth+'</td></tr>'
				+ '</table>' + '</div>' + '</div>';
			var infowindow = new google.maps.InfoWindow({
				content : contentString
			});
			var marker = new google.maps.Marker({
				position : {
					lat : latitude,
					lng : longitude
				},
				label : lab,
				map : map
			});
			marker.addListener('click', function() {
				infowindow.open(map, marker);
			});
		}
	    //This function initializes the map and show it on the jsp page,
	    //centring it on the coordinate of Milan
		function initMap() {
			mapDiv = document.getElementById('map');
			lab = 'c';
			map = new google.maps.Map(mapDiv, {
				center : {
					lat : 45.4642,
					lng : 9.1895
				},
				zoom : 8
			});
		}
	
	</script>

	<script async defer
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDvapmGzTY9LkbiSPqd_OI1SYvYi5HFnNE&callback=initMap">
		
	</script>

	<script>
		<c:forEach items="${publicUserData}" var="data">

		var country = '${data.country}';
		var city = '${data.city}';
		var street = String('${data.street}');
		if ('${data.username}' == '${loggedUser.username}') {
			loggedUserIsPublic = true;
		}
		// The Google Geocoding API url used to get the JSON data
		var geocodingAPI = "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ street + ",+" + city + ",+" + country + "&sensor=true";
		$.getJSON(geocodingAPI, function(json) {
			latitude = json.results[0].geometry.location.lat;
			longitude = json.results[0].geometry.location.lng;

			var publicUser = '${data.username}';
			addMarker(latitude, longitude, map, publicUser);
		});

		</c:forEach>
		if (loggedUserIsPublic == false) {
			addLoggedMarker();
		}
	</script>

	<div id="result" style="color: red"></div>
	<br/>
	<div class="container">
		<form method="post" id="signup">
		<div class="sep"></div>
		<div class = "inputs">
			<spring:bind path="elabUser.country">
				<input type="text" placeholder="Country" id="country"
					name="${status.expression}" value="${status.value}">
				<br />
			</spring:bind>

			<spring:bind path="elabUser.city">
				<input type="text" placeholder="City" id="city"
					name="${status.expression}" value="${status.value}">
				<br />
			</spring:bind>

			<spring:bind path="elabUser.street">
				<input type="text" placeholder="Street" id="street"
					name="${status.expression}" value="${status.value}">
				<br />
			</spring:bind>

			<input type="text" placeholder="House Number" id="houseNum">
			<br />
			
			<spring:bind path="elabUser.zipCode">
				<input type="hidden" id="zipCode" name="${status.expression}" >
				<br />
			</spring:bind>
			
			<spring:bind path="user.username">
				<input type="hidden" name="${status.expression}" value="${user.username}">
				<br />
			</spring:bind>

			<spring:bind path="user.oid">
				<input type="hidden" name="${status.expression}" value="${user.oid}">
				<br />
			</spring:bind>
			<p style="margin-top:0px; font-size:13px">By filling this form you consent to show your data to the other users of this portal.</p>
			<input type="submit" value="Confirm" id="submit" 
				onclick="myFunction()"> <br />
		</div>
		</form>
		</div>
	
</body>
</html>



