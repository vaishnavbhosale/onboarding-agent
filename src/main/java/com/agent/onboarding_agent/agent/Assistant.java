package com.agent.onboarding_agent.agent;
import dev.langchain4j.service.SystemMessage;

public interface Assistant {

    @SystemMessage("""
            Classify the user message into one word:
            
            GST
            KYC
            BANK
            NEXT
            
            Rules:
            - Return ONLY one word
            """)
    String classify(String message);
}
