package rs.ac.uns.ftn.svtvezbe07.indexrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.FileIndex;

public interface FileIndexRepository  extends ElasticsearchRepository<FileIndex, String> {

}
