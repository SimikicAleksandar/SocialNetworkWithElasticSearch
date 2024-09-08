package rs.ac.uns.ftn.svtvezbe07.indexrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtvezbe07.indexmodel.PostIndex;

@Repository
public interface PostIndexRepository extends ElasticsearchRepository<PostIndex, Long>  {

}
