package it.perigea.formazione.aggregator.configuration;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import it.perigea.formazione.comune.ClinicalStatusDto;
import it.perigea.formazione.comune.SomministrationsDto;


@EnableKafka
@Configuration
public class KafkaConsumerConfig{

	@Autowired
	private KafkaProperties kafkaProperties;

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Autowired
	private KafkaTemplate<String, SomministrationsDto> kafkaTemplate;

	@Autowired
	private KafkaTemplate<String, ClinicalStatusDto> clinicalKafkaTemplate;

	@Value("${topicName}")
	private String topicName; 

	@Value("${ClinicalTopicName}")
	private String ClinicalTopicName;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(KafkaConsumerConfig.class);

	private KafkaConsumer<String, SomministrationsDto> consumer;

	private KafkaConsumer<String, ClinicalStatusDto> clinicalConsumer;
}