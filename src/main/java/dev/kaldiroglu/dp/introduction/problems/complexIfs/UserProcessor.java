package dev.kaldiroglu.dp.introduction.problems.complexIfs;

public class UserProcessor {

    /**
     * A ridiculously complicated method that processes user data
     * with deeply nested if statements - an example of what NOT to do!
     */
    public String processUser(User user, boolean isAdmin, String userType,
                                  int accessLevel, boolean hasPermissions,
                                  String department, boolean isActive) {

        if (user != null) {
            if (user.getAge() >= 18) {
                if (isActive) {
                    if (userType != null) {
                        if (userType.equals("PREMIUM")) {
                            if (isAdmin) {
                                if (accessLevel > 5) {
                                    if (hasPermissions) {
                                        if (department != null) {
                                            if (department.equals("IT")) {
                                                if (user.getExperience() > 2) {
                                                    if (user.getCertifications().size() > 3) {
                                                        if (user.getLastLoginDays() < 30) {
                                                            if (user.getFailedLogins() < 3) {
                                                                return "FULL_ACCESS_GRANTED";
                                                            } else {
                                                                if (user.getFailedLogins() < 5) {
                                                                    return "LIMITED_ACCESS_SECURITY_REVIEW";
                                                                } else {
                                                                    return "ACCESS_BLOCKED_TOO_MANY_FAILURES";
                                                                }
                                                            }
                                                        } else {
                                                            if (user.getLastLoginDays() < 90) {
                                                                return "ACCESS_GRANTED_PASSWORD_RESET_REQUIRED";
                                                            } else {
                                                                return "ACCOUNT_DORMANT_REACTIVATION_NEEDED";
                                                            }
                                                        }
                                                    } else {
                                                        if (user.getCertifications().size() > 1) {
                                                            return "PARTIAL_ACCESS_CERTIFICATION_PENDING";
                                                        } else {
                                                            return "TRAINING_REQUIRED";
                                                        }
                                                    }
                                                } else {
                                                    if (user.getExperience() > 1) {
                                                        return "SUPERVISED_ACCESS_ONLY";
                                                    } else {
                                                        return "INTERN_ACCESS_BASIC_ONLY";
                                                    }
                                                }
                                            } else if (department.equals("HR")) {
                                                if (user.getBackgroundCheck()) {
                                                    if (user.getComplianceTraining()) {
                                                        return "HR_FULL_ACCESS";
                                                    } else {
                                                        return "HR_LIMITED_COMPLIANCE_TRAINING_NEEDED";
                                                    }
                                                } else {
                                                    return "HR_ACCESS_DENIED_BACKGROUND_CHECK";
                                                }
                                            } else if (department.equals("FINANCE")) {
                                                if (user.getFinancialClearance()) {
                                                    if (user.getAuditScore() > 85) {
                                                        return "FINANCE_FULL_ACCESS";
                                                    } else {
                                                        if (user.getAuditScore() > 70) {
                                                            return "FINANCE_RESTRICTED_ACCESS";
                                                        } else {
                                                            return "FINANCE_AUDIT_REQUIRED";
                                                        }
                                                    }
                                                } else {
                                                    return "FINANCE_CLEARANCE_PENDING";
                                                }
                                            } else {
                                                if (user.getGeneralTraining()) {
                                                    return "GENERAL_DEPARTMENT_ACCESS";
                                                } else {
                                                    return "BASIC_ACCESS_TRAINING_REQUIRED";
                                                }
                                            }
                                        } else {
                                            return "ACCESS_DENIED_NO_DEPARTMENT";
                                        }
                                    } else {
                                        if (accessLevel > 3) {
                                            return "MODERATE_ACCESS_NO_PERMISSIONS";
                                        } else {
                                            return "BASIC_ACCESS_ONLY";
                                        }
                                    }
                                } else {
                                    if (accessLevel > 2) {
                                        return "NON_ADMIN_MODERATE_ACCESS";
                                    } else {
                                        return "NON_ADMIN_BASIC_ACCESS";
                                    }
                                }
                            } else {
                                if (hasPermissions) {
                                    if (accessLevel > 3) {
                                        return "NON_ADMIN_PREMIUM_HIGH_ACCESS";
                                    } else {
                                        return "NON_ADMIN_PREMIUM_STANDARD_ACCESS";
                                    }
                                } else {
                                    return "PREMIUM_USER_LIMITED_ACCESS";
                                }
                            }
                        } else if (userType.equals("STANDARD")) {
                            if (isAdmin) {
                                if (accessLevel > 4) {
                                    return "ADMIN_STANDARD_HIGH_ACCESS";
                                } else {
                                    return "ADMIN_STANDARD_NORMAL_ACCESS";
                                }
                            } else {
                                if (hasPermissions) {
                                    return "STANDARD_USER_WITH_PERMISSIONS";
                                } else {
                                    if (user.getAccountAge() > 365) {
                                        return "STANDARD_USER_VETERAN";
                                    } else {
                                        return "STANDARD_USER_BASIC";
                                    }
                                }
                            }
                        } else if (userType.equals("TRIAL")) {
                            if (user.getTrialDaysRemaining() > 0) {
                                if (user.getTrialDaysRemaining() > 7) {
                                    return "TRIAL_ACTIVE_FULL_FEATURES";
                                } else {
                                    return "TRIAL_EXPIRING_SOON_LIMITED";
                                }
                            } else {
                                return "TRIAL_EXPIRED_UPGRADE_REQUIRED";
                            }
                        } else {
                            return "UNKNOWN_USER_TYPE_DEFAULT_ACCESS";
                        }
                    } else {
                        return "NULL_USER_TYPE_ACCESS_DENIED";
                    }
                } else {
                    if (user.getSuspensionReason() != null) {
                        if (user.getSuspensionReason().equals("TEMPORARY")) {
                            return "ACCOUNT_TEMPORARILY_SUSPENDED";
                        } else {
                            return "ACCOUNT_PERMANENTLY_SUSPENDED";
                        }
                    } else {
                        return "INACTIVE_ACCOUNT_REASON_UNKNOWN";
                    }
                }
            } else {
                if (user.getAge() >= 16) {
                    if (user.getParentalConsent()) {
                        return "MINOR_ACCESS_WITH_CONSENT";
                    } else {
                        return "MINOR_ACCESS_PARENTAL_CONSENT_REQUIRED";
                    }
                } else {
                    return "ACCESS_DENIED_TOO_YOUNG";
                }
            }
        } else {
            return "NULL_USER_ACCESS_DENIED";
        }
    }
}