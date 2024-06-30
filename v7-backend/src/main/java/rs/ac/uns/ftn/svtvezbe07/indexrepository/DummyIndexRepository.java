package rs.ac.uns.ftn.svtvezbe07.indexrepository;

import rs.ac.uns.ftn.svtvezbe07.indexmodel.DummyIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyIndexRepository
    extends ElasticsearchRepository<DummyIndex, String> {
}
