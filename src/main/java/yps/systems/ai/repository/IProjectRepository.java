package yps.systems.ai.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Project;

@Repository
public interface IProjectRepository extends Neo4jRepository<Project, String> {

    @Query("MATCH (pr:Project), (tm:Team) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(tm) = $teamElementId " +
            "CREATE (pr)-[:HAS_TEAM]->(tm) " +
            "RETURN pr")
    Project setTeamToProject(String projectElementId, String teamElementId);

    @Query("MATCH (pr:Project), (pe:Person) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(pe) = $personElementId " +
            "CREATE (pr)-[:HAS_TUTOR]->(pe) " +
            "RETURN pr")
    Project setTutorToProject(String projectElementId, String personElementId);

    @Query("MATCH (pr:Project), (ob:Objective) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(ob) = $objectiveElementId " +
            "CREATE (pr)-[:HAS_OBJECTIVE]->(ob) " +
            "RETURN pr")
    Project setObjectiveToProject(String projectElementId, String objectiveElementId);

    @Query("MATCH (pr:Project), (ts:Task) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(ts) = $taskElementId " +
            "CREATE (pr)-[:HAS_TASK]->(ts) " +
            "RETURN pr")
    Project setTaskToProject(String projectElementId, String taskElementId);

    @Query("MATCH (pr:Project)-[r:HAS_TEAM]->(tm:Team) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(tm) = $teamElementId " +
            "DELETE r")
    void removeTeamFromProject(String projectElementId, String teamElementId);

    @Query("MATCH (pr:Project)-[r:HAS_TUTOR]->(pe:Person) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(pe) = $personElementId " +
            "DELETE r")
    void removeTutorFromProject(String projectElementId, String personElementId);

    @Query("MATCH (pr:Project)-[r:HAS_OBJECTIVE]->(ob:Objective) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(ob) = $objectiveElementId " +
            "DELETE r")
    void removeObjectiveFromProject(String projectElementId, String objectiveElementId);

    @Query("MATCH (pr:Project)-[r:HAS_TASK]->(ts:Task) " +
            "WHERE elementId(pr) = $projectElementId " +
            "AND elementId(ts) = $taskElementId " +
            "DELETE r")
    void removeTaskFromProject(String projectElementId, String taskElementId);
}
