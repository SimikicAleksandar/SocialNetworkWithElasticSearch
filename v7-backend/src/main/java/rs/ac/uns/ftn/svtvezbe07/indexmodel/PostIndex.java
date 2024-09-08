package rs.ac.uns.ftn.svtvezbe07.indexmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.annotation.Nullable;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class PostIndex {
    @Id
    private Long id;

    @Field(type = FieldType.Text, store = true, name = "title")
    private String title;

    @Nullable
    @Field(type = FieldType.Text, store = true, name = "content_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contentSr;

    @Nullable
    @Field(type = FieldType.Text, store = true, name = "content_en", analyzer = "english", searchAnalyzer = "english")
    private String contentEn;

    @Field(type = FieldType.Date, store = true, name = "creation_date")
    private LocalDate creationDate;

    @Field(type = FieldType.Integer, store = true, name = "like_number")
    private Integer likeNumber;


    public PostIndex(Long id, String title, LocalDate creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.likeNumber = 0;
    }
}
