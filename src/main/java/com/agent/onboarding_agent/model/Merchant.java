package com.agent.onboarding_agent.model;

public class Merchant {

    private String name;
    private String gst;

    private OnboardingStatus status;
    private boolean gstVerified;
    private boolean kycDone;
    private boolean bankVerified;

    public Merchant() {
    }

    public Merchant(String name, String gst ) {
        this.name = name;
        this.gst = gst;

    }

    public String getName() {
        return name;
    }

    public String getGst() {
        return gst;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public void setStatus(OnboardingStatus status) {
        this.status = status;
    }
    public OnboardingStatus getStatus() {
        return status;
    }

    public boolean isGstVerified() { return gstVerified; }
    public void setGstVerified(boolean gstVerified) { this.gstVerified = gstVerified; }

    public boolean isKycDone() { return kycDone; }
    public void setKycDone(boolean kycDone) { this.kycDone = kycDone; }

    public boolean isBankVerified() { return bankVerified; }
    public void setBankVerified(boolean bankVerified) { this.bankVerified = bankVerified; }
}
