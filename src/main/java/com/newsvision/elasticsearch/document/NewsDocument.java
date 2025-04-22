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
@Document(indexName = "news") // Elasticsearch 인덱스 이름
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsDocument {

    @Id
    private Long id;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "standard"),
            otherFields = {
                    @InnerField(suffix = "kor", type = FieldType.Text, analyzer = "korean_analyzer"),
                    @InnerField(suffix = "eng", type = FieldType.Text, analyzer = "english_analyzer"),
                    @InnerField(suffix = "mixed", type = FieldType.Text, analyzer = "korean_english_analyzer")
            })
    private String title;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "standard"),
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

    @Field(type = FieldType.Text)
    private String image;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private String createdAt;

}
