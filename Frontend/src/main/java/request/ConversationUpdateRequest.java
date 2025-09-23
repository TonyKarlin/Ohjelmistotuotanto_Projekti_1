package request;

import lombok.Data;

@Data
public class ConversationUpdateRequest {

    String name;

    public ConversationUpdateRequest() {}

    public ConversationUpdateRequest(String name) {
        this.name = name;
    }
}
