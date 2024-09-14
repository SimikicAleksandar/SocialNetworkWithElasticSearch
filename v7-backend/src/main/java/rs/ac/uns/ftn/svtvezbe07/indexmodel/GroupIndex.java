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
@Document(indexName = "group")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class GroupIndex {
    @Id
    private Long id;
    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String name;

    @Nullable
    @Field(type = FieldType.Text, store = true, name = "description_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String descriptionSr;

    @Nullable
    @Field(type = FieldType.Text, store = true, name = "description_en", analyzer = "english", searchAnalyzer = "english")
    private String descriptionEn;

    @Field(type = FieldType.Date, store = true, name = "creation_date")
    private LocalDate creationDate;

    @Field(type = FieldType.Boolean, store = true, name = "suspended")
    private boolean isSuspended;

    @Field(type = FieldType.Text, store = true, name = "suspended_reason")
    private String suspendedReason;

    @Field(type = FieldType.Integer, store = true, name = "post_number")
    private Integer postNumber;

    @Field(type = FieldType.Text, store = true, name = "admin_id")
    private Long adminId;

    @Field(type = FieldType.Boolean, store = true, name = "deleted")
    private Boolean deleted;

    public GroupIndex(Long id,  String name ,LocalDate creationDate, boolean isSuspended, String suspendedReason) {
        this.creationDate = creationDate;
        this.isSuspended = isSuspended;
        this.suspendedReason = suspendedReason;
        this.name = name;
        this.id = id;
        this.postNumber = 0;
    }
}


