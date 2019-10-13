metadata {
    definition(name: "Ambient Weather Device", namespace: "CordMaster", author: "Alden Howard") {
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "Pressure Measurement"
        capability "Illuminance Measurement"
        capability "Refresh"
        capability "Sensor"
        capability "Actuator"

        //Current Conditions
        attribute "weather", "string"
        attribute "weatherIcon", "string"
        attribute "dewPoint", "number"
        attribute "comfort", "number"
        attribute "feelsLike", "number"
        attribute "pressure", "string"

        //Precipitation
        attribute "precip_today", "number"
        attribute "precip_1hr", "number"


        //Wind
        attribute "wind", "number"
        attribute "wind_gust", "number"
        attribute "wind_degree", "number"
        attribute "wind_dir", "string"
        attribute "wind_direction", "string"

        //Light
        attribute "solarradiation", "number"
        attribute "uv", "number"

        //Indoor
        attribute "indoortemperature", "number"
        attribute "indoorhumidity", "number"
    }
    preferences {
        section("Preferences") {
            input "showLogs", "bool", required: false, title: "Show Debug Logs?", defaultValue: false
        }
    }
}

def refresh() {
    parent.fetchNewWeather();
}

def setWeather(weather) {
    logger("debug", "Weather: " + weather);

    //Set temperature
    sendEvent(name: "temperature", value: weather.tempf, unit: '°F', isStateChange: true);

    //Set Humidity
    sendEvent(name: "humidity", value: weather.humidity, unit: '%', isStateChange: true);

    //Set DewPoint
    sendEvent(name: "dewPoint", value: weather.dewPoint, unit:'°F', isStateChange: true);

    //Set Comfort Level
    float temp = 0.0;

    temp = (weather.dewPoint - 35);
    if (temp <= 0) {
        temp = 0.0;
    } else if (temp >= 40.0) {
        temp = 100.0;
    } else {
        temp = (temp / 40.0) * 100.0;
    }
    temp = temp.round(1);
    sendEvent(name: "comfort", value: temp, isStateChange: true);

    //Set Barometric Pressure
    sendEvent(name: "pressure", value: weather.baromrelin, unit: 'in', isStateChange: true);

    //Set Feels Like Temperature
    sendEvent(name: "feelsLike", value: weather.feelsLike, unit: '°F', isStateChange: true);

    //Rain
    sendEvent(name: "precip_today", value: weather.dailyrainin, unit: 'in', isStateChange: true);
    sendEvent(name: "precip_1hr", value: weather.hourlyrainin, unit: 'in', isStateChange: true);

    //Wind
    sendEvent(name: "wind", value: weather.windspeedmph, unit: 'mph', isStateChange: true);
    sendEvent(name: "wind_gust", value: weather.windgustmph, unit: 'mph', isStateChange: true);
    sendEvent(name: "wind_degree", value: weather.winddir, unit: '°', isStateChange: true);

    temp = weather.winddir
    if (temp < 22.5) {
        sendEvent(name:  "wind_direction", value: "North", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "N", isStateChange: true);
    } else if (temp < 67.5) {
        sendEvent(name:  "wind_direction", value: "Northeast", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "NE", isStateChange: true);
    } else if (temp < 112.5) {
        sendEvent(name: "wind_direction", value: "East", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "E", isStateChange: true);
    } else if (temp < 157.5) {
        sendEvent(name: "wind_direction", value: "Southeast", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "SE", isStateChange: true);
    } else if (temp < 202.5) {
        sendEvent(name: "wind_direction", value: "South", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "S", isStateChange: true);
    } else if (temp < 247.5) {
        sendEvent(name: "wind_direction", value: "Southwest", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "SW", isStateChange: true);
    } else if (temp < 292.5) {
        sendEvent(name: "wind_direction", value: "West", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "W", isStateChange: true);
    } else if (temp < 337.5) {
        sendEvent(name: "wind_direction", value: "Northwest", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "NW", isStateChange: true);
    } else                      {
        sendEvent(name:  "wind_direction", value: "North", isStateChange: true);
        sendEvent(name:  "wind_dir", value: "N", isStateChange: true);
    }

    //UV and Light
    sendEvent(name: "solarradiation", value: weather.solarradiation, isStateChange: true);
    sendEvent(name: "illuminance", value: weather.solarradiation, isStateChange: true);
    sendEvent(name: "uv", value: weather.uv, isStateChange: true);

    //Indoor Temp
    sendEvent(name: "indoortemperature", value: weather.tempinf, unit: '°F', isStateChange: true);
    
    //Indoor Humidity
    sendEvent(name: "indoorhumidity", value: weather.humidityin, unit: '%', isStateChange: true)

    if (weather.containsKey('temp1f')) {
        // We will assume all the item 1 keys exist
        sendEvent(name: "temperature1", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity1", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp2f')) {
        // We will assume all the item 2 keys exist
        sendEvent(name: "temperature2", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity2", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp3f')) {
        // We will assume all the item 3 keys exist
        sendEvent(name: "temperature3", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity3", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp4f')) {
        // We will assume all the item 4 keys exist
        sendEvent(name: "temperature4", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity4", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp5f')) {
        // We will assume all the item 5 keys exist
        sendEvent(name: "temperature5", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity5", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp6f')) {
        // We will assume all the item 6 keys exist
        sendEvent(name: "temperature6", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity6", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp7f')) {
        // We will assume all the item 7 keys exist
        sendEvent(name: "temperature7", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity7", value: weather.humidity1, unit: '%', isStateChange: true)
    }

    if (weather.containsKey('temp8f')) {
        // We will assume all the item 8 keys exist
        sendEvent(name: "temperature8", value: weather.temp1f, unit: '°F', isStateChange: true);
        sendEvent(name: "humidity8", value: weather.humidity1, unit: '%', isStateChange: true)
    }
}

private logger(type, msg){
    if(type && msg && settings?.showLogs) {
        log."${type}" "${msg}"
    }
}

