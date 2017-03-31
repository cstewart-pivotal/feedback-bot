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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class DemoService
{

	@Value("${io.pivotal.ecosystem.roadshow}")
	private String url;

	private static final String SENTIMENT = "sentiment";
	private static final String SCORE = "score";
	private static final String MAGNITUDE = "magnitude";


	public DemoServiceResult build(String text)
	{
		String jsonResult = callPythonApp(url, text);

		JSONObject json = new JSONObject(jsonResult);

		System.out.println("json result:" + jsonResult);

		JSONObject sentiment = json.getJSONObject(SENTIMENT);
		double score = sentiment.getDouble(SCORE);
		double magnitude = sentiment.getDouble(MAGNITUDE);

		DemoServiceResult result = new DemoServiceResult();
		result.setMagnitude(magnitude);
		result.setScore(score);

		//opportunity to refactor? By splitting up into seperate method?
		if ((score >= 0.5) && (magnitude >= 0.5)){
			String negativeResponse = "Awesome! Thanks for the great feedback! Keep on rockin'!";
			result.setResponse(negativeResponse);
		}
		else if((score <= -0.1) && (magnitude >= 0.5)){
			String positiveResponse = "Oh dear, I'm so sorry to hear that. What can we do to make it up to you?";
			result.setResponse(positiveResponse);
		}
		else {
			String neutralResponse = "I don't know what to tell you... ";
			result.setResponse(neutralResponse);
		}
        System.out.println("result:" + result);
		return result;
	}

	@HystrixCommand(fallbackMethod = "reliable")
	public String callPythonApp(String url, String text){
		RestTemplate restTemplate = new RestTemplate();
		String userInput = "{\"request\":\"" + text + "\"}";
		String jsonResult = restTemplate.postForObject(url, userInput, String.class);
		return jsonResult;
	}


	public String reliable(String url, String text){
		Resource resource = new ClassPathResource("/default_response.json");
		String content = null;
		try{
			content = new String(Files.readAllBytes(Paths.get(resource.getURI())));
			content = content.replaceAll("REPLACE ME", text + " this was an error");

		}
		catch (IOException e){
			System.err.println("error reading json file");
			e.printStackTrace();
		}
		return content;
	}



}

