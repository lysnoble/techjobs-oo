package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.data.JobFieldData;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, Job ajob, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view

        ajob = jobData.findById(id);
        if(ajob!=null){
            model.addAttribute("ajob", ajob);
            return "job-detail";
        }
        else {
            return "new-job";
        }
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.
        if(errors.hasErrors()){
            model.addAttribute("jobForm", jobForm);
            return "new-job";
        } else {
            String aName = jobForm.getName();


            int aemp = jobForm.getEmployerId();
            Employer aEmployer =  jobData.getEmployers().findById(aemp);
            int aloc = jobForm.getLocationId();
            Location aLocation = jobData.getLocations().findById(aloc);
            int askl= jobForm.getSkillId();
            CoreCompetency aSkill = jobData.getCoreCompetencies().findById(askl);
            int apos = jobForm.getPositionId();
            PositionType aPositionType = jobData.getPositionTypes().findById(apos);
            Job aJob = new Job(aName, aEmployer, aLocation, aPositionType, aSkill);
            jobData.add(aJob);
            int id = aJob.getId();
            model.addAttribute("ajob", aJob);
            model.addAttribute("id", id);
            return "redirect:/job?id="+id;
        }

    }
}
