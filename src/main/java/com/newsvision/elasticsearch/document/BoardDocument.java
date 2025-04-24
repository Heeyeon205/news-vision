package com.newsvision.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "boards")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardDocument {

    @Id
    private Long id;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "korean_english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "kor", type = FieldType.Text, analyzer = "korean_analyzer"),
                    @InnerField(suffix = "eng", type = FieldType.Text, analyzer = "english_analyzer"),
                    @InnerField(suffix = "mixed", type = FieldType.Text, analyzer = "korean_english_analyzer")
            })
    private String content;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Keyword)
    private String username;

    @Field(type = FieldType.Keyword, index = false)
    private String image;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Integer)
    private int view;

    @Field(type = FieldType.Long)
    private Long newsId;

    @Field(type = FieldType.Boolean)
    private Boolean isReported;

    @Field(type = FieldType.Integer)
    private int likeCount;

    @Field(type = FieldType.Integer)
    private int commentCount;

    @Field(type = FieldType.Keyword)
    private String nickname;

    @Field(type = FieldType.Keyword, index = false)
    private String userImage;

    @Field(type = FieldType.Keyword, index = false)
    private String icon;
}
