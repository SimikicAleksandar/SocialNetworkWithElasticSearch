package rs.ac.uns.ftn.svtvezbe07.repository;

import rs.ac.uns.ftn.svtvezbe07.model.entity.DummyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<DummyTable, Integer> {
}
