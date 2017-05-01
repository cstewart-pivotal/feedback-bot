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

public class SentimentModel
{
	private String request;
	private String response;
	private String magnitude;
	private String score;

	
	public String getRequest()
	{
		return request;
	}
	public void setRequest(String request)
	{
		this.request = request;
	}

	public String getResponse() { return response; }
	public void setResponse(String response) { this.response = response; }

	public String getScore(){ return score; }
	public void setScore(String score) { this.score = score; }

	public String getMagnitude(){ return magnitude; }
	public void setMagnitude(String magnitude) { this.magnitude = magnitude; }
	
	@Override
	public String toString()
	{
		return "SentimentModel [request=" + request + ", response=" + response + ", magnitude=" + magnitude + ", score=" + score + "]";

	}
	
}


