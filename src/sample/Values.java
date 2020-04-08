package sample;

import Simulation.Simulation;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Values {

    public static final String defaultShiftsFileName = "DefaultShifts.txt";
    public static final String defaultMealsFileName = "DefaultMeals.txt";
    public static final String defaultFoodFileName = "DefaultFoodItems.txt";
    public static final String defaultWaypointFileName = "DefaultWaypoints.txt";

    public static Simulation simulation;

    public static Stage primaryStage;
    public static BorderPane rootPage;

    public static final double foodScrollFrameHeight = Double.MAX_VALUE * .70;

    public static final double foodListWidth = 550;

    public static final double sideMenuWidth = 250;
    public static final double sideMenuHeight = Double.MAX_VALUE;
    public static final double sideMenuBtnHeight = 100;
    public static final double sideMenuBtnWidth = Double.MAX_VALUE;

    public static final int numberOfMenues = 4;
    public static final Integer mapMenuID = 1;
    public static final Integer foodMenuID = 2;
    public static final Integer mealsMenuID = 3;
    public static final Integer shiftsMenuID = 4;

    public static final String googleMapsAPIKey = "AIzaSyCC1c7VEIHPU08i-fNQEkfps0S4i-TsU9I";
    public static final String googleMapsJavaScript = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title>Places Search Box</title>\n" +
            "    <style>\n" +
            "      /* Always set the map height explicitly to define the size of the div\n" +
            "       * element that contains the map. */\n" +
            "      #map {\n" +
            "        height: 100%;\n" +
            "      }\n" +
            "      /* Optional: Makes the sample page fill the window. */\n" +
            "      html, body {\n" +
            "        height: 100%;\n" +
            "        margin: 0;\n" +
            "        padding: 0;\n" +
            "      }\n" +
            "      #description {\n" +
            "        font-family: Roboto;\n" +
            "        font-size: 15px;\n" +
            "        font-weight: 300;\n" +
            "      }\n" +
            "\n" +
            "      #infowindow-content .title {\n" +
            "        font-weight: bold;\n" +
            "      }\n" +
            "\n" +
            "      #infowindow-content {\n" +
            "        display: none;\n" +
            "      }\n" +
            "\n" +
            "      #map #infowindow-content {\n" +
            "        display: inline;\n" +
            "      }\n" +
            "\n" +
            "      .pac-card {\n" +
            "        margin: 10px 10px 0 0;\n" +
            "        border-radius: 2px 0 0 2px;\n" +
            "        box-sizing: border-box;\n" +
            "        -moz-box-sizing: border-box;\n" +
            "        outline: none;\n" +
            "        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);\n" +
            "        background-color: #fff;\n" +
            "        font-family: Roboto;\n" +
            "      }\n" +
            "\n" +
            "      #pac-container {\n" +
            "        padding-bottom: 12px;\n" +
            "        margin-right: 12px;\n" +
            "      }\n" +
            "\n" +
            "      .pac-controls {\n" +
            "        display: inline-block;\n" +
            "        padding: 5px 11px;\n" +
            "      }\n" +
            "\n" +
            "      .pac-controls label {\n" +
            "        font-family: Roboto;\n" +
            "        font-size: 13px;\n" +
            "        font-weight: 300;\n" +
            "      }\n" +
            "\n" +
            "      #pac-input {\n" +
            "        background-color: #fff;\n" +
            "        font-family: Roboto;\n" +
            "        font-size: 15px;\n" +
            "        font-weight: 300;\n" +
            "        margin-left: 12px;\n" +
            "        padding: 0 11px 0 13px;\n" +
            "        text-overflow: ellipsis;\n" +
            "        width: 400px;\n" +
            "      }\n" +
            "\n" +
            "      #pac-input:focus {\n" +
            "        border-color: #4d90fe;\n" +
            "      }\n" +
            "\n" +
            "      #title {\n" +
            "        color: #fff;\n" +
            "        background-color: #4d90fe;\n" +
            "        font-size: 25px;\n" +
            "        font-weight: 500;\n" +
            "        padding: 6px 12px;\n" +
            "      }\n" +
            "      #target {\n" +
            "        width: 345px;\n" +
            "      }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<input id=\"pac-input\" class=\"controls\" type=\"text\" placeholder=\"Search Box\">\n" +
            "<div id=\"map\"></div>\n" +
            "<script type=\"text/javascript\">\n" +
            "\n" +
            "      var lastLat = 0;\n" +
            "      var lastLng = 0;\n" +
            "\n" +
            "      function sendToJava (lat, lon) {\n" +
            "            javaConnector.sendLatLong(lat, lon);\n" +
            "        };\n" +
            "\n" +
            "        var jsConnector = {\n" +
            "            addMarker: function (name) {\n" +
            "              var myLatLng = {lat: 41.15516, lng: -80.07863};\n" +
            "              var marker = new google.maps.Marker({\n" +
            "                  position: myLatLng,\n" +
            "                  map: map,\n" +
            "                  title: 'name'\n" +
            "              });\n" +
            "            }\n" +
            "        };\n" +
            "\n" +
            "        function getJsConnector() {\n" +
            "            return jsConnector;\n" +
            "        };\n" +
            "\n" +
            "      // This example adds a search box to a map, using the Google Place Autocomplete\n" +
            "      // feature. People can enter geographical searches. The search box will return a\n" +
            "      // pick list containing a mix of places and predicted search terms.\n" +
            "\n" +
            "      // This example requires the Places library. Include the libraries=places\n" +
            "      // parameter when you first load the API. For example:\n" +
            "      // <script src=\"https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places\">\n" +
            "\n" +
            "      function initAutocomplete() {\n" +
            "        var map = new google.maps.Map(document.getElementById('map'), {\n" +
            "          center: {lat: 41.154984, lng: -80.078031},\n" +
            "          zoom: 18,\n" +
            "          mapTypeId: 'roadmap'\n" +
            "        });\n" +
            "\n" +
            "        google.maps.event.addListener(map, 'click', function( event ){\n" +
            "          sendToJava(event.latLng.lat(), event.latLng.lng());\n" +
            "          lastLat = event.latLng.lat();\n" +
            "          lastLng = event.latLng.lng();\n" +
            "        });\n" +
            "\n" +
            "        // Create the search box and link it to the UI element.\n" +
            "        var input = document.getElementById('pac-input');\n" +
            "        var searchBox = new google.maps.places.SearchBox(input);\n" +
            "        map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);\n" +
            "\n" +
            "        // Bias the SearchBox results towards current map's viewport.\n" +
            "        map.addListener('bounds_changed', function() {\n" +
            "          searchBox.setBounds(map.getBounds());\n" +
            "        });\n" +
            "\n" +
            "        var markers = [];\n" +
            "        // Listen for the event fired when the user selects a prediction and retrieve\n" +
            "        // more details for that place.\n" +
            "        searchBox.addListener('places_changed', function() {\n" +
            "          var places = searchBox.getPlaces();\n" +
            "\n" +
            "          if (places.length == 0) {\n" +
            "            return;\n" +
            "          }\n" +
            "\n" +
            "          // Clear out the old markers.\n" +
            "          markers.forEach(function(marker) {\n" +
            "            marker.setMap(null);\n" +
            "          });\n" +
            "          markers = [];\n" +
            "\n" +
            "          // For each place, get the icon, name and location.\n" +
            "          var bounds = new google.maps.LatLngBounds();\n" +
            "          places.forEach(function(place) {\n" +
            "            if (!place.geometry) {\n" +
            "              console.log(\"Returned place contains no geometry\");\n" +
            "              return;\n" +
            "            }\n" +
            "            var icon = {\n" +
            "              url: place.icon,\n" +
            "              size: new google.maps.Size(71, 71),\n" +
            "              origin: new google.maps.Point(0, 0),\n" +
            "              anchor: new google.maps.Point(17, 34),\n" +
            "              scaledSize: new google.maps.Size(25, 25)\n" +
            "            };\n" +
            "\n" +
            "            // Create a marker for each place.\n" +
            "            markers.push(new google.maps.Marker({\n" +
            "              map: map,\n" +
            "              icon: icon,\n" +
            "              title: place.name,\n" +
            "              position: place.geometry.location\n" +
            "            }));\n" +
            "\n" +
            "            if (place.geometry.viewport) {\n" +
            "              // Only geocodes have viewport.\n" +
            "              bounds.union(place.geometry.viewport);\n" +
            "            } else {\n" +
            "              bounds.extend(place.geometry.location);\n" +
            "            }\n" +
            "          });\n" +
            "          map.fitBounds(bounds);\n" +
            "        });\n" +
            "      }\n" +
            "\n" +
            "    </script>\n" +
            "<script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCC1c7VEIHPU08i-fNQEkfps0S4i-TsU9I&libraries=places&callback=initAutocomplete\"\n" +
            "        async defer></script>\n" +
            "</body>\n" +
            "</html>";

}
