package dev.kaldiroglu.dp.creational.introduction.employee.better;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {
    private PersonalInfo personal;
    private ContactInfo contact;
    private JobInfo job;
    private WorkInfo work;
    private Compensation compensation;

    public Employee(PersonalInfo personal, ContactInfo contact, JobInfo job, WorkInfo work, Compensation compensation) {
        this.personal = (personal == null)
                ? new PersonalInfo("", "", null, "", "", "")
                : personal;
        this.contact = (contact == null)
                ? new ContactInfo("", "", "", "")
                : contact;
        this.job = (job == null)
                ? new JobInfo("", "", "", "", LocalDate.now(), "", "")
                : job;


        if (this.job.getEmploymentType() == null || this.job.getEmploymentType().isEmpty()) {
            this.job.setEmploymentType("Full-time");
        }

        this.work = (work == null)
                ? new WorkInfo("", false, "EST")
                : work;

        // Ensure work defaults
        if (this.work.getTimeZone() == null || this.work.getTimeZone().isEmpty()) {
            this.work.setTimeZone("EST");
        }
        // remoteWorker is a primitive boolean; default is false when using no-arg WorkInfo.

        this.compensation = (compensation == null) ? new Compensation(BigDecimal.ZERO) : compensation;
    }

    public PersonalInfo getPersonal() {
        return personal;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public JobInfo getJob() {
        return job;
    }

    public WorkInfo getWork() {
        return work;
    }

    public Compensation getCompensation() {
        return compensation;
    }
}