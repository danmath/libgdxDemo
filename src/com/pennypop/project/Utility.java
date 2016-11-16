package com.pennypop.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;

public class Utility {
	public static float toCelcius(float kelvin) {
		return kelvin-(float)(273.15);
	}
	
	public static float toFarenheit(float celcius) {
		return (celcius*((float)1.8))+32;
	}
	
	public static float toMPH(float mps) {
		return (360*mps/((float)1609.344));
	}
	
	public static float roundToHundreth(float num) {
		BigDecimal bd = new BigDecimal(Float.toString(num));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
	}
	
	public static OrderedMap<String, Object> HttpResponseReader(HttpResponse httpResponse){

        BufferedReader read = new BufferedReader(new InputStreamReader(httpResponse.getResultAsStream()));  
        StringBuffer result = new StringBuffer();
        String line = "";

        try {
          while ((line = read.readLine()) != null) {
                  result.append(line);
              }

              OrderedMap<String, Object> orderedMap;
            try {
                orderedMap = (OrderedMap<String, Object>) new JsonReader().parse(result.toString());
                return orderedMap;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
