package backend_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfiguration {

    @Value("${chat.topic.path}")
    private String topicPath;

    public String getTopicPath() {
        return topicPath;
    }
}
