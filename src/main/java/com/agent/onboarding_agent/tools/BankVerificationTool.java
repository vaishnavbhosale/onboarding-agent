package com.agent.onboarding_agent.tools;

import com.agent.onboarding_agent.service.MerchantService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BankVerificationTool {
    @Autowired
    private MerchantService merchantService;

@Tool("Verify the bank account details of a merchant using their GST number")
public String verifyBank(@P("gst number of the merchant") String gst) {
    return merchantService.verifyBank(gst);
}
}