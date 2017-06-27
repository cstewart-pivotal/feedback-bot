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

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DemoService
{
	@Value("${io.pivotal.ecosystem.roadshow}")
	private String url;
	
	private SentimentService sentimentService;

	private static final String SENTIMENT = "sentiment";
	private static final String SCORE = "score";
	private static final String MAGNITUDE = "magnitude";
	private static final String REQUEST = "request";

	public DemoService(SentimentService service)
	{
		this.sentimentService = service;
	}

	public DemoServiceResult build(String text)
	{
		String jsonResult = sentimentService.callPythonApp(url, text);

		JSONObject json = new JSONObject(jsonResult);

		String request = json.getString(REQUEST);

		JSONObject sentiment = json.getJSONObject(SENTIMENT);
		double score = sentiment.getDouble(SCORE);
		double magnitude = sentiment.getDouble(MAGNITUDE);

		DemoServiceResult result = new DemoServiceResult();
		result.setMagnitude(magnitude);
		result.setScore(score);

		if((request.equals("Service Down")) && (score == 0.0) && (magnitude == 0.0)){
			String fallbackResponse = "Insert meaningful experience here";
			result.setResponse(fallbackResponse);
		}

		else if((score > 0) && (magnitude >= 0.5)){
			String positiveResponse = "Awesome! Thanks for the great feedback! Keep on rockin'!";
			result.setResponse(positiveResponse);
		}
		else if((score < 0) && (magnitude >= 0.5)){
			String negativeResponse = "Oh dear, I'm so sorry to hear that. What can we do to make it up to you?";
			result.setResponse(negativeResponse);
		}
		else {
			String neutralResponse = "I don't know what to tell you... ";
			result.setResponse(neutralResponse);
		}

		return result;
	}
}

