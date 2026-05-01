package com.agent.onboarding_agent.controller;

import com.agent.onboarding_agent.agent.OnboardingAgent;
import com.agent.onboarding_agent.mock.MockDB;
import com.agent.onboarding_agent.model.Merchant;
import com.agent.onboarding_agent.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MerchantController {

    @Autowired
    private OnboardingAgent agent;

    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        return agent.chat(message);
    }

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/onboard")
    public String onboard(@RequestBody Merchant merchant) {
        return merchantService.onboardMerchant(merchant);
    }
    @GetMapping("/merchants")
    public Map<String, Merchant> getAllMerchants() {
        return MockDB.merchantDB;
    }
    @PostMapping("/verify-gst")
    public String verifyGST(@RequestParam String gst) {
        return merchantService.verifyGST(gst);
    }
    @PostMapping("/verify-kyc")
    public String verifyKYC(@RequestParam String gst) {
        return merchantService.verifyKYC(gst);
    }
    @PostMapping("/verify-bank")
    public String verifyBank(@RequestParam String gst) {
        return merchantService.verifyBank(gst);
    }
    @PostMapping("/complete")
    public String complete(@RequestParam String gst) {
        return merchantService.completeOnboarding(gst);
    }
    @GetMapping("/status")
    public Merchant getStatus(@RequestParam String gst) {
        return MockDB.merchantDB.get(gst);
    }
}