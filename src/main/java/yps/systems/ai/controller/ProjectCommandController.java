package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Project;
import yps.systems.ai.object.ProjectObjective;
import yps.systems.ai.object.ProjectPerson;
import yps.systems.ai.object.ProjectTask;
import yps.systems.ai.object.ProjectTeam;
import yps.systems.ai.repository.IProjectRepository;

import java.util.Optional;

@RestController
@RequestMapping("/command/projectService")
public class ProjectCommandController {

    private final IProjectRepository projectRepository;
    private final KafkaTemplate<String, Project> kafkaTemplate;

    @Value("${env.kafka.topicEvent}")
    private String kafkaTopicEvent;

    @Autowired
    public ProjectCommandController(IProjectRepository projectRepository, KafkaTemplate<String, Project> kafkaTemplate) {
        this.projectRepository = projectRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<String> savePersonNode(@RequestBody Project project) {
        Project createdProject = projectRepository.save(project);
        Message<Project> message = MessageBuilder
                .withPayload(createdProject)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "CREATE_PROJECT")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return new ResponseEntity<>("Project saved with ID: " + createdProject.getElementId(), HttpStatus.CREATED);
    }

    @PostMapping("/setTeam")
    public ResponseEntity<String> setTeamToProject(@RequestBody ProjectTeam projectTeam) {
        projectRepository.setTeamToProject(projectTeam.projectElementId(), projectTeam.teamElementId());
        Message<ProjectTeam> message = MessageBuilder
                .withPayload(projectTeam)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "SET_TEAM")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Team set to project successfully");
    }

    @PostMapping("/setTutor")
    public ResponseEntity<String> setTutorToProject(@RequestBody ProjectPerson projectPerson) {
        projectRepository.setTutorToProject(projectPerson.projectElementId(), projectPerson.personElementId());
        Message<ProjectPerson> message = MessageBuilder
                .withPayload(projectPerson)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "SET_TUTOR")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Tutor set to project successfully");
    }

    @PostMapping("/setObjective")
    public ResponseEntity<String> setObjectiveToProject(@RequestBody ProjectObjective projectObjective) {
        projectRepository.setObjectiveToProject(projectObjective.projectElementId(), projectObjective.objectiveElementId());
        Message<ProjectObjective> message = MessageBuilder
                .withPayload(projectObjective)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "SET_OBJECTIVE")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Objective set to project successfully");
    }

    @PostMapping("/setTask")
    public ResponseEntity<String> setTaskToProject(@RequestBody ProjectTask projectTask) {
        projectRepository.setTaskToProject(projectTask.projectElementId(), projectTask.taskElementId());
        Message<ProjectTask> message = MessageBuilder
                .withPayload(projectTask)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "SET_TASK")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Task set to project successfully");
    }

    @PutMapping("/{elementId}")
    public ResponseEntity<String> updateProject(@PathVariable String elementId, @RequestBody Project project) {
        Optional<Project> optionalProject = projectRepository.findById(elementId);
        if (optionalProject.isPresent()) {
            project.setElementId(optionalProject.get().getElementId());
            projectRepository.save(project);
            Message<Project> message = MessageBuilder
                    .withPayload(project)
                    .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                    .setHeader("eventType", "UPDATE_PROJECT")
                    .setHeader("source", "projectService")
                    .build();
            kafkaTemplate.send(message);
            return ResponseEntity.ok("Project updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProject(@RequestParam String projectElementId) {
        projectRepository.deleteById(projectElementId);
        Message<String> message = MessageBuilder
                .withPayload(projectElementId)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "DELETE_PROJECT")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Project deleted successfully");
    }

    @DeleteMapping("/removeTeam")
    public ResponseEntity<String> removeTeamFromProject(@RequestBody ProjectTeam projectTeam) {
        projectRepository.removeTeamFromProject(projectTeam.projectElementId(), projectTeam.teamElementId());
        Message<ProjectTeam> message = MessageBuilder
                .withPayload(projectTeam)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "REMOVE_TEAM")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Team removed from project successfully");
    }

    @DeleteMapping("/removeTutor")
    public ResponseEntity<String> removeTutorFromProject(@RequestBody ProjectPerson projectPerson) {
        projectRepository.removeTutorFromProject(projectPerson.projectElementId(), projectPerson.personElementId());
        Message<ProjectPerson> message = MessageBuilder
                .withPayload(projectPerson)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "REMOVE_TUTOR")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Tutor removed from project successfully");
    }

    @DeleteMapping("/removeObjective")
    public ResponseEntity<String> removeObjectiveFromProject(@RequestBody ProjectObjective projectObjective) {
        projectRepository.removeObjectiveFromProject(projectObjective.projectElementId(), projectObjective.objectiveElementId());
        Message<ProjectObjective> message = MessageBuilder
                .withPayload(projectObjective)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "REMOVE_OBJECTIVE")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Objective removed from project successfully");
    }

    @DeleteMapping("/removeTask")
    public ResponseEntity<String> removeTaskFromProject(@RequestBody ProjectTask projectTask) {
        projectRepository.removeTaskFromProject(projectTask.projectElementId(), projectTask.taskElementId());
        Message<ProjectTask> message = MessageBuilder
                .withPayload(projectTask)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicEvent)
                .setHeader("eventType", "REMOVE_TASK")
                .setHeader("source", "projectService")
                .build();
        kafkaTemplate.send(message);
        return ResponseEntity.ok("Task removed from project successfully");
    }

}
