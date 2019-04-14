package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project, Long projectId) {
        if (projectId != null) {
            if (project.getId() != projectId) {
                throw new ProjectIdException("Project id mismatched");
            }

            if (projectRepository.findById(projectId) == null) {
                throw new ProjectIdException("Project not found");
            }
        }

        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception ex) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exist");
        }

        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = findProjectByIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException("Cannot find Project with ID '" + projectId.toUpperCase());
        }

        projectRepository.delete(project);
    }
}
