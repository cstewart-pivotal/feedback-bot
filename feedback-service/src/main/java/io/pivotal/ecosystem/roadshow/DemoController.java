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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;


@Controller
public class DemoController
{
	private static final Logger LOG = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	private DemoService service;

	@GetMapping("/")
	public String showForm(Model model)
	{
		model.addAttribute("model", new SentimentModel());
		String template = "index";
		LOG.debug("returning template " + template);
		return template;
	}

	@RequestMapping(value="/process", method=RequestMethod.POST)
	public String processForm(@ModelAttribute("model") SentimentModel model)
	{
		DemoServiceResult result = service.build(model.getRequest());

		model.setScore(Double.toString(result.getScore()));
		model.setMagnitude(Double.toString(result.getMagnitude()));
		model.setResponse(result.getResponse());

		LOG.debug("model = " + model);

		String template = "sentiment";
		LOG.debug("returning template " + template);
		return "sentiment";
	}
}
