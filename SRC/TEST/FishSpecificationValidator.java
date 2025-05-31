package SRC.TEST;

import SRC.VALIDATION.FishSpecificationValidation;

/**
 * Test class for Fish Specification Validation
 * Runs all 12 fish requirements and generates a comprehensive report
 */
public class FishSpecificationValidator {
    
    public static void main(String[] args) {
        System.out.println("Starting Fish Specification Validation...");
        System.out.println();
        
        FishSpecificationValidation validator = new FishSpecificationValidation();
        
        // Run all validations
        boolean allRequirementsMet = validator.validateAllRequirements();
        
        // Print detailed report
        validator.printValidationReport();
        
        // Summary
        if (allRequirementsMet) {
            System.out.println("🎉 SUCCESS: All fish specification requirements are met!");
            System.out.println("The fishing system is complete and properly configured.");
        } else {
            System.out.println("⚠️  WARNING: Some fish specification requirements are not met.");
            System.out.println("Review the failed requirements above to improve the fishing system.");
            
            // Show specific recommendations
            System.out.println();
            System.out.println("RECOMMENDATIONS:");
            for (String error : validator.getValidationErrors()) {
                if (error.contains("REQ1")) {
                    System.out.println("- Add more fish types to meet minimum diversity requirements");
                } else if (error.contains("REQ2")) {
                    System.out.println("- Ensure all locations (Pond, Forest River, Mountain Lake, Ocean) have fish");
                } else if (error.contains("REQ3")) {
                    System.out.println("- Add fish for missing seasons to improve seasonal variety");
                } else if (error.contains("REQ4")) {
                    System.out.println("- Add fish with different weather requirements (SUNNY, RAINY, ANY)");
                } else if (error.contains("REQ5")) {
                    System.out.println("- Add more fish available during daytime hours (6:00-18:00)");
                } else if (error.contains("REQ6")) {
                    System.out.println("- Add more fish available during nighttime hours (18:00-6:00)");
                } else if (error.contains("REQ7")) {
                    System.out.println("- Make legendary fish more unique with distinct conditions");
                } else if (error.contains("REQ8")) {
                    System.out.println("- Make common fish more accessible (ANY season/weather)");
                } else if (error.contains("REQ9")) {
                    System.out.println("- Better distribute regular fish across different seasons");
                } else if (error.contains("REQ10")) {
                    System.out.println("- Add location-specific fish to make each location unique");
                } else if (error.contains("REQ11")) {
                    System.out.println("- Balance seasonal distribution to avoid overrepresentation");
                } else if (error.contains("REQ12")) {
                    System.out.println("- Adjust fish energy values to match their type (Common:1-3, Regular:4-8, Legendary:9+)");
                }
            }
        }
        
        System.out.println();
        System.out.println("Validation completed.");
    }
    
    /**
     * Run validation without printing to console (for testing purposes)
     * @return true if all requirements are met
     */
    public static boolean runSilentValidation() {
        FishSpecificationValidation validator = new FishSpecificationValidation();
        return validator.validateAllRequirements();
    }
    
    /**
     * Get detailed validation results
     * @return FishSpecificationValidation object with results
     */
    public static FishSpecificationValidation getValidationResults() {
        FishSpecificationValidation validator = new FishSpecificationValidation();
        validator.validateAllRequirements();
        return validator;
    }
}
