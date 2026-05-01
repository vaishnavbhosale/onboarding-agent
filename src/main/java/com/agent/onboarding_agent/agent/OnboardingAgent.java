package com.agent.onboarding_agent.agent;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import com.agent.onboarding_agent.tools.BankVerificationTool;
import com.agent.onboarding_agent.tools.GSTValidationTool;
import com.agent.onboarding_agent.tools.KYCTool;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.agent.onboarding_agent.service.MerchantService;

@Component
public class OnboardingAgent {

    private final Assistant assistant;
    private final MerchantService merchantService;

    public OnboardingAgent(
            GSTValidationTool gstTool,
            KYCTool kycTool,
            BankVerificationTool bankTool,
            MerchantService merchantService,
            @Value("${my.api.key}")
            String apiKey
    )
    {

        GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-flash-latest")
                .build();

        this.assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .tools(gstTool, kycTool, bankTool)
                .build();

        this.merchantService = merchantService;
    }

    public String chat(String message) {

        String intent = assistant.classify(message).toUpperCase().trim();
        String gst = extractGST(message);

        if (gst == null) {
            return "Please provide GST number";
        }

        if (intent.contains("GST")) {
            return merchantService.verifyGST(gst);
        }
        else if (intent.contains("KYC")) {
            return merchantService.verifyKYC(gst);
        }
        else if (intent.contains("BANK")) {
            return merchantService.verifyBank(gst);
        }
        else if (intent.contains("NEXT")) {
            return merchantService.getNextStep(gst);
        }
        else {
            return "Try GST, KYC, Bank or ask 'what next?'";
        }
    }
    private String extractGST(String message) {
        for (String part : message.split(" ")) {
            if (part.matches("[0-9A-Z]{10,15}")) {
                return part;
            }
        }
        return null;
    }
}