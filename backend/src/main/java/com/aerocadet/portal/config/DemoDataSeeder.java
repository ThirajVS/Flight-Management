package com.aerocadet.portal.config;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name="app.demo-data.enabled",havingValue="true")
public class DemoDataSeeder implements ApplicationRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;
    public DemoDataSeeder(JdbcTemplate jdbc,PasswordEncoder passwordEncoder){this.jdbc=jdbc;this.passwordEncoder=passwordEncoder;}
    @Override @Transactional public void run(ApplicationArguments args){
        Integer existing=jdbc.queryForObject("SELECT COUNT(*) FROM users",Integer.class);
        if(existing!=null&&existing>0)return;
        String hash=passwordEncoder.encode("AeroCadetDemo!2026");
        Long candidateRole=roleId("ROLE_CANDIDATE"), recruiterRole=roleId("ROLE_RECRUITER"), adminRole=roleId("ROLE_ADMIN");
        long[] candidates=new long[20];
        for(int i=1;i<=20;i++){
            long user=insertUser("Cadet Demo %02d".formatted(i),"candidate%02d@demo.aerocadet.local".formatted(i),hash);
            candidates[i-1]=user; addRole(user,candidateRole);
            jdbc.update("INSERT INTO candidate_profiles (user_id,phone,date_of_birth,nationality,city,state,country,profile_completion,tenth_percentage,twelfth_percentage,physics_marks,mathematics_marks,english_marks,graduation_details,medical_status,total_flight_hours,english_proficiency,passport_available,preferred_program,preferred_training_location,available_from) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    user,"+91 90000 %05d".formatted(i),LocalDate.of(2001+(i%5),1+(i%11),1+(i%25)),"Indian",List.of("Bengaluru","Hyderabad","Pune","Chennai").get(i%4),"Demo State","India",85,
                    72+i%12,70+i%15,68+i%18,74+i%14,76+i%10,"Synthetic university record","PENDING_VERIFICATION",i%8,"ICAO_LEVEL_4_OR_HIGHER",true,"Airline Cadet Pilot Program","Hyderabad",LocalDate.of(2027,1,1));
        }
        long[] recruiters=new long[5];
        for(int i=1;i<=5;i++){recruiters[i-1]=insertUser("Recruitment Officer %02d".formatted(i),"recruiter%02d@demo.aerocadet.local".formatted(i),hash);addRole(recruiters[i-1],recruiterRole);}
        long[] admins=new long[3];
        for(int i=1;i<=3;i++){admins[i-1]=insertUser("Platform Admin %02d".formatted(i),"admin%02d@demo.aerocadet.local".formatted(i),hash);addRole(admins[i-1],adminRole);}
        String[] statuses={"SUBMITTED","ELIGIBILITY_CHECK","DOCUMENT_VERIFICATION","SHORTLISTED","ASSESSMENT","INTERVIEW","MEDICAL","FINAL_SELECTION","OFFERED","REJECTED"};
        for(int i=1;i<=30;i++){
            long candidate=candidates[(i-1)%20]; long program=i<=20?((i-1)%10)+1:((i)%10)+1;
            String status=statuses[(i-1)%statuses.length]; String number="AC-2026-%05d".formatted(i);
            long app=insertApplication(number,candidate,program,status,i);
            jdbc.update("INSERT INTO application_status_history (application_id,status,changed_by,remarks,created_at) VALUES (?,?,?,?,CURRENT_TIMESTAMP)",app,status,recruiters[(i-1)%5],"Synthetic demo status");
            String[] stages={"Application","Eligibility","Aptitude Test","Technical Assessment","Interview","Medical","Final Selection"};
            for(int s=0;s<stages.length;s++) jdbc.update("INSERT INTO selection_stages (application_id,stage_order,stage,status) VALUES (?,?,?,?)",app,s+1,stages[s],s<Math.min(1+(i%7),7)?"COMPLETED":"NOT_STARTED");
            jdbc.update("INSERT INTO documents (application_id,document_type,original_filename,stored_filename,content_type,file_size,status,uploaded_at) VALUES (?,?,?,?,?,?,?,CURRENT_TIMESTAMP)",app,"PASSPORT","dummy-passport-%02d.pdf".formatted(i),"demo-%02d-passport.pdf".formatted(i),"application/pdf",2048,i%3==0?"VERIFIED":"PENDING_VERIFICATION");
            jdbc.update("INSERT INTO notifications (recipient_id,type,title,message,is_read,created_at) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)",candidate,"APPLICATION","Demo application update",number+" status is "+status,i%2==0);
            jdbc.update("INSERT INTO audit_logs (actor_id,action,entity_type,entity_id,details,created_at) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)",recruiters[(i-1)%5],"APPLICATION_STATUS_CHANGED","APPLICATION",Long.toString(app),"Synthetic seed event");
            if(i<=12) jdbc.update("INSERT INTO assessment_attempts (application_id,candidate_id,started_at,submitted_at,score,status,answers_json) VALUES (?,?,?,?,?,?,?)",app,candidate,Timestamp.from(Instant.now().minus(2,ChronoUnit.DAYS)),Timestamp.from(Instant.now().minus(2,ChronoUnit.DAYS).plus(20,ChronoUnit.MINUTES)),65+i*2,i>=3?"QUALIFIED":"NOT_QUALIFIED","{}");
            if(i<=8) jdbc.update("INSERT INTO interviews (application_id,candidate_id,scheduled_at,mode,location,interviewer,remarks,status) VALUES (?,?,?,?,?,?,?,?)",app,candidate,Timestamp.from(Instant.now().plus(i,ChronoUnit.DAYS)),i%2==0?"VIDEO":"IN_PERSON",i%2==0?"https://meet.example.invalid/demo":"AeroCadet Demo Centre","Recruitment Officer %02d".formatted((i%5)+1),"Synthetic interview","SCHEDULED");
        }
    }
    private long insertUser(String name,String email,String hash){KeyHolder keys=new GeneratedKeyHolder();jdbc.update(connection->{PreparedStatement ps=connection.prepareStatement("INSERT INTO users (full_name,email,password_hash,enabled,created_at) VALUES (?,?,?,?,CURRENT_TIMESTAMP)",new String[]{"id"});ps.setString(1,name);ps.setString(2,email);ps.setString(3,hash);ps.setBoolean(4,true);return ps;},keys);return keys.getKeyAs(Long.class);}
    private long insertApplication(String number,long candidate,long program,String status,int offset){KeyHolder keys=new GeneratedKeyHolder();jdbc.update(connection->{PreparedStatement ps=connection.prepareStatement("INSERT INTO applications (application_number,candidate_id,program_id,status,current_step,draft_data_json,submitted_at,created_at,updated_at) VALUES (?,?,?,?,7,'{}',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)",new String[]{"id"});ps.setString(1,number);ps.setLong(2,candidate);ps.setLong(3,program);ps.setString(4,status);return ps;},keys);return keys.getKeyAs(Long.class);}
    private Long roleId(String name){return jdbc.queryForObject("SELECT id FROM roles WHERE name=?",Long.class,name);}
    private void addRole(long user,long role){jdbc.update("INSERT INTO user_roles (user_id,role_id) VALUES (?,?)",user,role);}
}

