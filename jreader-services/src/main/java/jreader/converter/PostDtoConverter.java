package jreader.converter;

import jreader.domain.Post;
import jreader.dto.PostDto;

import org.springframework.core.convert.converter.Converter;

public class PostDtoConverter implements Converter<Post, PostDto> {

    @Override
    public PostDto convert(final Post entity) {
        final PostDto dto = new PostDto();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setAuthor(entity.getAuthor());
        dto.setLink(entity.getLink());
        dto.setPublishDate(entity.getPublishDate());
        dto.setRead(entity.isRead());
        dto.setBookmarked(entity.isBookMarked());
        dto.setId(String.valueOf(entity.getId()));
        if (entity.getSubscription() != null) {
            dto.setSubscriptionTitle(entity.getSubscription().getTitle());
            dto.setSubscriptionId(String.valueOf(entity.getSubscription().getId()));
            if (entity.getSubscription().getGroup() != null) {
                dto.setGroupId(String.valueOf(entity.getSubscription().getGroup().getId()));
            }
        }
        return dto;
    }

}
