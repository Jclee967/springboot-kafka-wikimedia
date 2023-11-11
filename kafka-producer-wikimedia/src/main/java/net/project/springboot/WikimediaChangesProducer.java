package net.project.springboot;

import com.launchdarkly.eventsource.EventSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WikimediaChangesProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic = "wikimedia_recent_change";

        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        WikimediaChangesHandler wikimediaChangesHandler = new WikimediaChangesHandler(kafkaTemplate, topic);
        EventSource.Builder builder = new EventSource.Builder(wikimediaChangesHandler, URI.create(url));
        EventSource eventSource = builder.build();
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);
    }
}
