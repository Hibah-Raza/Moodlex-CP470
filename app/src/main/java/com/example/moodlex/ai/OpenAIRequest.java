package com.example.moodlex.ai;

import java.util.ArrayList;
import java.util.List;

public class OpenAIRequest {

    public String model = "gpt-3.5-turbo";
    public List<Message> messages;

    public OpenAIRequest(String prompt) {
        messages = new ArrayList<>();
        messages.add(new Message("user", prompt));
    }

    public static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
