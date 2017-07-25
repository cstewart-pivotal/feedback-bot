package io.pivotal.ecosystem.roadshow;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class SentimentService {
	private final RestTemplate restTemplate;

	public SentimentService(RestTemplate rest) {
		this.restTemplate = rest;
	}
	
	public String callPythonApp(String url, String text) {
		String userInput = "{\"request\":\"" + text + "\"}";
		String jsonResult = restTemplate.postForObject(url, userInput, String.class);

		return jsonResult;
	}
}

































//	public String skipPythonApp(String url, String text) {
//		System.out.println("skipPythonApp method called");
//		Resource resource = new ClassPathResource("/fallback_response.json");
//		String content = null;
//		try {
//			content = new String(Files.readAllBytes(Paths.get(resource.getURI())));
//		}
//		catch (IOException e){
//			System.err.println("error reading json file");
//			e.printStackTrace();
//		}
//		return content;
//
//	}