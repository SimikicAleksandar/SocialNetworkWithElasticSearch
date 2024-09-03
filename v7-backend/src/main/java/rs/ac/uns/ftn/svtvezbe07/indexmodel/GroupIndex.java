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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "group")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class GroupIndex {
    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String description;

    @Field(type = FieldType.Date, store = true, name = "creation_date")
    private LocalDate creationDate;

    @Field(type = FieldType.Text, store = true, name = "admin_id")
    private Long adminId;

    @Field(type = FieldType.Boolean, store = true, name = "deleted")
    private Boolean deleted;
}


