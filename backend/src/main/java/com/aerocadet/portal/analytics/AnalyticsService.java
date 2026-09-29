package com.aerocadet.portal.analytics;

import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aerocadet.portal.application.ApplicationStatus;
import com.aerocadet.portal.application.CadetApplication;
import com.aerocadet.portal.application.CadetApplicationRepository;
import com.aerocadet.portal.document.ApplicationDocumentRepository;
import com.aerocadet.portal.document.DocumentStatus;
import com.aerocadet.portal.interview.InterviewRepository;
import com.aerocadet.portal.program.CadetProgramRepository;
import com.aerocadet.portal.program.ProgramStatus;
import com.aerocadet.portal.user.CandidateProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalyticsService {
    private final CandidateProfileRepository profileRepository;
    private final CadetProgramRepository programRepository;
    private final CadetApplicationRepository applicationRepository;
    private final ApplicationDocumentRepository documentRepository;
    private final InterviewRepository interviewRepository;
    public AnalyticsService(CandidateProfileRepository profileRepository,CadetProgramRepository programRepository,
            CadetApplicationRepository applicationRepository,ApplicationDocumentRepository documentRepository,
            InterviewRepository interviewRepository){this.profileRepository=profileRepository;this.programRepository=programRepository;
        this.applicationRepository=applicationRepository;this.documentRepository=documentRepository;this.interviewRepository=interviewRepository;}
    @Transactional(readOnly=true)
    public Summary summary(){
        List<CadetApplication> applications=applicationRepository.findAll();
        Map<String,Long> byStatus=applications.stream().collect(Collectors.groupingBy(a->a.getStatus().name(),LinkedHashMap::new,Collectors.counting()));
        Map<String,Long> byProgram=applications.stream().collect(Collectors.groupingBy(a->a.getProgram().getName(),LinkedHashMap::new,Collectors.counting()));
        Map<String,Long> monthly=applications.stream().collect(Collectors.groupingBy(a->YearMonth.from(a.getCreatedAt().atZone(ZoneOffset.UTC)).toString(),LinkedHashMap::new,Collectors.counting()));
        Map<String,Long> funnel=new LinkedHashMap<>();
        long registered=profileRepository.count(); long applied=applications.stream().map(a->a.getCandidate().getId()).distinct().count();
        funnel.put("Registered",registered);funnel.put("Applied",applied);
        funnel.put("Eligible",countAtOrBeyond(applications,ApplicationStatus.ELIGIBILITY_CHECK));
        funnel.put("Shortlisted",countAtOrBeyond(applications,ApplicationStatus.SHORTLISTED));
        funnel.put("Assessment",countAtOrBeyond(applications,ApplicationStatus.ASSESSMENT));
        funnel.put("Interview",countAtOrBeyond(applications,ApplicationStatus.INTERVIEW));
        funnel.put("Selected",applications.stream().filter(a->a.getStatus()==ApplicationStatus.OFFERED||a.getStatus()==ApplicationStatus.FINAL_SELECTION).count());
        return new Summary(registered,programRepository.countByStatus(ProgramStatus.OPEN),applications.size(),
                applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED),interviewRepository.countByStatus("SCHEDULED"),
                applicationRepository.countByStatus(ApplicationStatus.OFFERED),byStatus,byProgram,monthly,funnel);
    }
    @Transactional(readOnly=true)
    public RecruiterSummary recruiter(){return new RecruiterSummary(
            applicationRepository.countByStatus(ApplicationStatus.SUBMITTED),
            documentRepository.countByStatus(DocumentStatus.PENDING_VERIFICATION),
            applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED),
            interviewRepository.countByStatus("SCHEDULED"),
            applicationRepository.countByStatus(ApplicationStatus.ELIGIBILITY_CHECK)+applicationRepository.countByStatus(ApplicationStatus.DOCUMENT_VERIFICATION));}
    private long countAtOrBeyond(List<CadetApplication> apps,ApplicationStatus threshold){return apps.stream().filter(a->a.getStatus().ordinal()>=threshold.ordinal()&&a.getStatus()!=ApplicationStatus.REJECTED&&a.getStatus()!=ApplicationStatus.WITHDRAWN).map(a->a.getCandidate().getId()).distinct().count();}
    public record Summary(long totalCandidates,long activePrograms,long totalApplications,long shortlisted,long upcomingInterviews,long finalSelected,
                          Map<String,Long> applicationsByStatus,Map<String,Long> applicationsByProgram,Map<String,Long> monthlyApplications,Map<String,Long> candidateFunnel){}
    public record RecruiterSummary(long newApplications,long pendingDocuments,long shortlisted,long upcomingInterviews,long requiringAction){}
}

