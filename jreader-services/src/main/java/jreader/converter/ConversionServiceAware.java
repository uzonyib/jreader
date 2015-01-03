package jreader.converter;

import org.springframework.core.convert.ConversionService;

public interface ConversionServiceAware {
    
    void setConversionService(ConversionService conversionService);

}
