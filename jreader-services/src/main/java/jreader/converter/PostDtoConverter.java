package jreader.converter;

import jreader.domain.Post;
import jreader.dto.PostDto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostDtoConverter implements Converter<Post, PostDto> {

    @Override
    public PostDto convert(final Post entity) {
        String subscriptionId = null;
        String subscriptionTitle = null;
        String groupId = null;
        if (entity.getSubscription() != null) {
            subscriptionId = String.valueOf(entity.getSubscription().getId());
            subscriptionTitle = entity.getSubscription().getTitle();
            if (entity.getSubscription().getGroup() != null) {
                groupId = String.valueOf(entity.getSubscription().getGroup().getId());
            }
        }
        
        return PostDto.builder()
                .groupId(groupId)
                .subscriptionId(subscriptionId)
                .subscriptionTitle(subscriptionTitle)
                .title(entity.getTitle())
                .description(entity.getDescription())
                .author(entity.getAuthor())
                .link(entity.getLink())
                .publishDate(entity.getPublishDate())
                .read(entity.isRead())
                .bookmarked(entity.isBookmarked())
                .id(String.valueOf(entity.getId()))
                .build();
    }

}
