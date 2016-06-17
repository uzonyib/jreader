package jreader.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Cache
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {

    @Id
    private String url;
    private String title;
    private String description;
    private String feedType;
    private Long lastUpdateDate;
    private Long lastRefreshDate;
    private Integer status;

}
