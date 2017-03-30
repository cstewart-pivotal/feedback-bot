/**
 Copyright (C) 2017-Present Pivotal Software, Inc. All rights reserved.

 This program and the accompanying materials are made available under
 the terms of the under the Apache License, Version 2.0 (the "License”);
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package io.pivotal.ecosystem.roadshow;



import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class DemoService
{

	@Value("${io.pivotal.ecosystem.roadshow}")
	private String url;

	private static final String SENTIMENT = "sentiment";
	private static final String SCORE = "score";
	private static final String MAGNITUDE = "magnitude";

	//can add Hystrix fallback method here, or can split into two methods
	public DemoServiceResult build(String text)
	{

		//format user input into JSON
		String userInput = "{\"request\":\"" + text + "\"}";


		RestTemplate restTemplate = new RestTemplate();

		//String jsonResult = restTemplate.postForObject(url, userInput, String.class);
		String jsonResult = "{\"request\":\"I am really sad today\", \"sentiment\": {\"magnitude\": 0.4, \"score\": -0.4}}";
		System.out.println("jsonResult = " + jsonResult);

		JSONObject json = new JSONObject(jsonResult);

		JSONObject sentiment = json.getJSONObject(SENTIMENT);
		double score = sentiment.getDouble(SCORE);
		double magnitude = sentiment.getDouble(MAGNITUDE);

//		System.out.println("sentiment = " + sentiment);
//		System.out.println("score = " + score);
//		System.out.println("magnitude = " + magnitude);


		DemoServiceResult result = new DemoServiceResult();
		result.setMagnitude(magnitude);
		result.setScore(score);

		//opportunity to refactor? By splitting up into seperate method? 

		if ((score == -0.4) && (magnitude == 0.4)){
			String negativeResponse = "I'm really sorry to hear you're upset today";
			result.setResponse(negativeResponse);
		}
		else if((score == -0.8) && (magnitude == 0.4)){
			String positiveResponse = "Yay! I'm so stoked you're stoked!";
			result.setResponse(positiveResponse);
		}
		else {
			String neutralResponse = "I don't know what to tell you... ";
			result.setResponse(neutralResponse);
		}

		return result;
	}

}

