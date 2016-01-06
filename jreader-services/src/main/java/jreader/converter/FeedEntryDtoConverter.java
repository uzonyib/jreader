package jreader.converter;

import jreader.domain.FeedEntry;
import jreader.dto.FeedEntryDto;

import org.springframework.core.convert.converter.Converter;

public class FeedEntryDtoConverter implements Converter<FeedEntry, FeedEntryDto> {

    @Override
    public FeedEntryDto convert(final FeedEntry entity) {
        final FeedEntryDto dto = new FeedEntryDto();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setAuthor(entity.getAuthor());
        dto.setLink(entity.getLink());
        dto.setPublishDate(entity.getPublishDate());
        dto.setRead(entity.isRead());
        dto.setStarred(entity.isStarred());
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
