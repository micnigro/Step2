package it.perigea.formazione.extractor.controller;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.perigea.formazione.comune.SomministrationsDto;
import it.perigea.formazione.extractor.kafka.KafkaService;
import it.perigea.formazione.extractor.model.AbbreviationsDto;
import it.perigea.formazione.extractor.model.ExecutionDto;
import it.perigea.formazione.extractor.model.ProcessDto;
import it.perigea.formazione.extractor.service.ServiceInterface;

@RestController
@RequestMapping("/Vaccini")
public class SomministrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SomministrationController.class);
	
	private static final Logger logger =LoggerFactory.getLogger(SomministrationController.class);
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${topicName}")
	private String topicName;

	@Autowired
	private ServiceInterface sommService;
	
	@Autowired
	private KafkaService kafkaService;

	
	@GetMapping("/dataClient")
	public ResponseEntity<String> dataClient() {
		try {
			String resultRequest = sommService.dataClient();
			return ResponseEntity.ok().body("The process ended succesfully");
		} catch (Exception exc) {
			return ResponseEntity.ok().body("Status failure.");
		}
	}

	@GetMapping("/provinceExtractor")
	public ResponseEntity<List<AbbreviationsDto>> provinceClient() throws Exception {
		try {
			List<AbbreviationsDto> resultRequest = sommService.provinceClient();
			return ResponseEntity.ok().body(resultRequest);
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;

		}
	}

	@GetMapping("/fromJsonToJava")
	public ResponseEntity<String> fromJsonToJava() {
		try {
			List<SomministrationsDto> resultRequest = sommService.runProcess();
			return ResponseEntity.ok().body("The process ended succesfully");
		} catch (Exception exc) {
			return ResponseEntity.ok().body("Status failure.");
		}
	}

	@PostMapping(value = "/postSomm")
	@ResponseBody
	public List<SomministrationsDto> postJsonMessage() throws Exception {
		List<SomministrationsDto> listSommDto = sommService.runProcess();
		kafkaService.sendMessage(topicName, listSommDto);
		return listSommDto;
	}

	@GetMapping("/getListEsecutions")
	public ResponseEntity<List<ExecutionDto>> getAllEsecutions() {
		List<ExecutionDto> response = sommService.getAllEsecutions();
		if (!response.isEmpty()) {
			return ResponseEntity.ok().body(response);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/getProcessByDate")
	@ResponseBody
	public ResponseEntity<List<ProcessDto>> getProcessByDate(@RequestParam String date) throws ParseException {
		List<ProcessDto> resultRequest = sommService.getProcessByDate(date);
		return ResponseEntity.ok().body(resultRequest);
	}

	@GetMapping("/getListProcess")
	public ResponseEntity<List<ProcessDto>> getAllProcess() {
		List<ProcessDto> response = sommService.getAllProcess();
		if (!response.isEmpty()) {
			return ResponseEntity.ok().body(response);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
