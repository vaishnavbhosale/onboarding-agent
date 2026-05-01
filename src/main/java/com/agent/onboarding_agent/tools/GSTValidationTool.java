package com.agent.onboarding_agent.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;
import com.agent.onboarding_agent.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class GSTValidationTool {
    @Autowired
    private MerchantService merchantService;

@Tool("Verify a merchant's GST status")
public String isValidGST(@P("gst number of the merchant") String gst) {
    return merchantService.verifyGST(gst);
}
}