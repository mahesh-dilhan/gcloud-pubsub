package com.gcloud.gcloudpubsub;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@SpringBootApplication
public class GcloudPubsubApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcloudPubsubApplication.class, args);
	}
}
@Log4j2
@RestController
class PubSubController {

	@Autowired
	private OutboundHandler.ExternalMessageDeligator messagingGateway;

	@PostMapping("/publishMessage/{message}")
	public void publishMessage(@PathVariable String message) {
		messagingGateway.sendToPubsub(message);
		log.info("message send to pubsub {}"+ message);
	}

	@GetMapping("/")
	public String greet(){
		return "hello to pubsub";
	}
}