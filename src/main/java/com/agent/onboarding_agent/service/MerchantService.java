//package com.agent.onboarding_agent.service;
//
//import com.agent.onboarding_agent.mock.MockDB;
//import com.agent.onboarding_agent.model.Merchant;
//import com.agent.onboarding_agent.model.OnboardingStatus;
//import com.agent.onboarding_agent.tools.BankVerificationTool;
//import com.agent.onboarding_agent.tools.GSTValidationTool;
//import com.agent.onboarding_agent.tools.KYCTool;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class MerchantService {
//    @Autowired
//    private GSTValidationTool gstValidationTool;
//
//    @Autowired
//    private KYCTool kycTool;
//
//    @Autowired
//    private BankVerificationTool bankVerificationTool;
//
//        public String onboardMerchant(Merchant merchant) {
//
//            String gst = merchant.getGst();
//
//            if (merchant.getName() == null || merchant.getName().isEmpty()) {
//                return "Name cannot be empty ";
//            }
//            if (gst == null || gst.isEmpty()) {
//                return "GST cannot be empty ";
//            }
//
//            if (MockDB.merchantDB.containsKey(gst)) {
//                return "Merchant already exists ";
//            }
//            merchant.setStatus(OnboardingStatus.STARTED);
//
//            MockDB.merchantDB.put(gst, merchant);
//
//            return "Merchant onboarded successfully ";
//        }
//
//    public String verifyGST(String gst){
//            Merchant merchant = MockDB.merchantDB.get(gst);
//
//            if(merchant == null){
//                return "Merchant not found ";
//            }
//
//            if (!gstValidationTool.isValidGST(gst)) {
//            return "Invalid GST ";
//            }
//
//            if(merchant.getStatus() != OnboardingStatus.STARTED){
//                return "Merchant status not started or GST already verified ";
//            }
//             merchant.setStatus(OnboardingStatus.GST_VERIFIED);
//            return "GST verified successfully ";
//
//    }
//    public String verifyKYC(String gst) {
//
//        Merchant merchant = MockDB.merchantDB.get(gst);
//
//        if (merchant == null) {
//            return "Merchant not found ";
//        }
//
//        if (merchant.getStatus() != OnboardingStatus.GST_VERIFIED) {
//            return "Complete GST verification first ";
//        }
//        if (!kycTool.verifyKYC(gst)) {
//            return "KYC verification failed ";
//        }
//
//        merchant.setStatus(OnboardingStatus.KYC_DONE);
//
//        return "KYC completed ";
//    }
//    public String verifyBank(String gst) {
//
//        Merchant merchant = MockDB.merchantDB.get(gst);
//
//        if (merchant == null) {
//            return "Merchant not found ";
//        }
//
//        if (merchant.getStatus() != OnboardingStatus.KYC_DONE) {
//            return "Complete KYC first ";
//        }
//        if (!bankVerificationTool.verifyBank(gst)) {
//            return "Bank verification failed ";
//        }
//
//        merchant.setStatus(OnboardingStatus.BANK_VERIFIED);
//
//        return "Bank verified ✅";
//    }
//    public String completeOnboarding(String gst) {
//
//        Merchant merchant = MockDB.merchantDB.get(gst);
//
//        if (merchant == null) {
//            return "Merchant not found ";
//        }
//
//        if (merchant.getStatus() != OnboardingStatus.BANK_VERIFIED) {
//            return "Complete bank verification first ";
//        }
//
//        merchant.setStatus(OnboardingStatus.COMPLETED);
//
//        return "Onboarding completed ";
//    }
//    }
//
//
//
package com.agent.onboarding_agent.service;

import com.agent.onboarding_agent.mock.MockDB;
import com.agent.onboarding_agent.model.Merchant;
import com.agent.onboarding_agent.model.OnboardingStatus;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {

    public String onboardMerchant(Merchant merchant) {
        String gst = merchant.getGst();

        if (merchant.getName() == null || merchant.getName().isEmpty()) {
            return "Name cannot be empty";
        }
        if (gst == null || gst.isEmpty()) {
            return "GST cannot be empty";
        }

        if (MockDB.merchantDB.containsKey(gst)) {
            return "Merchant already exists";
        }

        merchant.setStatus(OnboardingStatus.STARTED);
        MockDB.merchantDB.put(gst, merchant);

        return "Merchant onboarded successfully";
    }

    public String verifyGST(String gst) {
        Merchant merchant = MockDB.merchantDB.get(gst);

        if (merchant == null) {
            return "Merchant not found";
        }

        // Logic moved directly here to avoid calling the Tool class
        if (gst.length() <= 5) {
            return "Invalid GST: Too short";
        }

        if (merchant.getStatus() != OnboardingStatus.STARTED) {
            return "GST already verified or process not started";
        }

        merchant.setStatus(OnboardingStatus.GST_VERIFIED);
        merchant.setGstVerified(true);
        return "GST verified  Next step: verify KYC";
    }

    public String verifyKYC(String gst) {
        Merchant merchant = MockDB.merchantDB.get(gst);

        if (merchant == null) {
            return "Merchant not found";
        }

        if (merchant.getStatus() != OnboardingStatus.GST_VERIFIED) {
            return "Complete GST verification first";
        }

        // KYC Mock Logic: Check if last digit is even
        char lastChar = gst.charAt(gst.length() - 1);
        boolean isKycValid = Character.isDigit(lastChar) && (lastChar - '0') % 2 == 0;

        if (!isKycValid) {
            return "KYC verification failed: Document mismatch";
        }

        merchant.setStatus(OnboardingStatus.KYC_DONE);
        merchant.setKycDone(true);
        return "KYC completed  Next step: verify Bank";
    }

    public String verifyBank(String gst) {
        Merchant merchant = MockDB.merchantDB.get(gst);

        if (merchant == null) {
            return "Merchant not found";
        }

        if (merchant.getStatus() != OnboardingStatus.KYC_DONE) {
            return "Complete KYC first";
        }

        // Bank Mock Logic
        if (gst.length() < 8) {
            return "Bank verification failed: Invalid account link";
        }

        merchant.setStatus(OnboardingStatus.BANK_VERIFIED);
        merchant.setBankVerified(true);
        return "Bank verified  Onboarding completed";
    }

    public String completeOnboarding(String gst) {
        Merchant merchant = MockDB.merchantDB.get(gst);

        if (merchant == null) {
            return "Merchant not found";
        }

        if (merchant.getStatus() != OnboardingStatus.BANK_VERIFIED) {
            return "Complete bank verification first";
        }

        merchant.setStatus(OnboardingStatus.COMPLETED);
        return "Onboarding completed successfully!";
    }

    public String getNextStep(String gst) {

        Merchant merchant = MockDB.merchantDB.get(gst);

        if (merchant == null) {
            return "Merchant not found. Please onboard first.";
        }

        if (!merchant.isGstVerified()) {
            return "GST pending -> please verify GST";
        }

        if (!merchant.isKycDone()) {
            return "KYC pending -> please verify KYC";
        }

        if (!merchant.isBankVerified()) {
            return "Bank pending -> please verify Bank";
        }

        return " All steps completed!";
    }
}
