package jreader.rss.converter;

import org.dozer.DozerConverter;

import com.sun.syndication.feed.synd.SyndContent;

public class DescriptionConverter extends DozerConverter<SyndContent, String> {
	
	public DescriptionConverter() {
		super(SyndContent.class, String.class);
	}

	@Override
	public String convertTo(SyndContent source, String destination) {
		return source.getValue();
	}

	@Override
	public SyndContent convertFrom(String source, SyndContent destination) {
		return null;
	}

}
