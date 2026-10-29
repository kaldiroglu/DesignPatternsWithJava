  package dev.kaldiroglu.dp.introduction.problems.user;

import java.util.List;

public class User {
    private int age;
    private int experience;
    private List<String> certifications;
    private int lastLoginDays;
    private int failedLogins;
    private boolean backgroundCheck;
    private boolean complianceTraining;
    private boolean financialClearance;
    private int auditScore;
    private boolean generalTraining;
    private int accountAge;
    private int trialDaysRemaining;
    private String suspensionReason;
    private boolean parentalConsent;

    public int getAge() {
        return age;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public int getLastLoginDays() {
        return lastLoginDays;
    }

    public int getFailedLogins() {
        return failedLogins;
    }

    public boolean getBackgroundCheck() {
        return backgroundCheck;
    }

    public boolean getComplianceTraining() {
        return complianceTraining;
    }

    public boolean getFinancialClearance() {
        return financialClearance;
    }

    public int getAuditScore() {
        return auditScore;
    }

    public boolean getGeneralTraining() {
        return generalTraining;
    }

    public int getAccountAge() {
        return accountAge;
    }

    public int getTrialDaysRemaining() {
        return trialDaysRemaining;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public boolean getParentalConsent() {
        return parentalConsent;
    }
}
