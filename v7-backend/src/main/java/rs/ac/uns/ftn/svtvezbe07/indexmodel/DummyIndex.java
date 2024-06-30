package rs.ac.uns.ftn.svtvezbe07.indexmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "dummy_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class DummyIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "title")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "content_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contentSr;

    @Field(type = FieldType.Text, store = true, name = "content_en", analyzer = "english", searchAnalyzer = "english")
    private String contentEn;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;

    @Field(type = FieldType.Integer, store = true, name = "database_id")
    private Integer databaseId;
}
