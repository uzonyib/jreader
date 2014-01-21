package jreader.converter;

import jreader.domain.SubscriptionGroup;
import jreader.dto.SubscriptionGroupDto;

import org.springframework.core.convert.converter.Converter;

public class SubscriptionGroupConverter implements Converter<SubscriptionGroup, SubscriptionGroupDto> {

	@Override
	public SubscriptionGroupDto convert(SubscriptionGroup entity) {
		SubscriptionGroupDto dto = new SubscriptionGroupDto();
		dto.setId(String.valueOf(entity.getId()));
		dto.setTitle(entity.getTitle());
		dto.setOrder(entity.getOrder());
		return dto;
	}

}
