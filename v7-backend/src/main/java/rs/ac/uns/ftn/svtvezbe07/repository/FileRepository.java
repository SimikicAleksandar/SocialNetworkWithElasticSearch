package rs.ac.uns.ftn.svtvezbe07.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svtvezbe07.model.entity.File;

public interface FileRepository extends JpaRepository<File, Integer> {
}
