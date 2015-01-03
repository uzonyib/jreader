package jreader.converter;

import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

public class ReaderConversionService implements FactoryBean<ConversionService>, InitializingBean {
    
    private Set<?> converters;

    private GenericConversionService conversionService;

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }

    @Override
    public void afterPropertiesSet() {
        conversionService = createConversionService();
        ConversionServiceFactory.registerConverters(converters, conversionService);
        for (Object converter : converters) {
            if (converter instanceof ConversionServiceAware) {
                ConversionServiceAware c = (ConversionServiceAware) converter;
                c.setConversionService(conversionService);
            }
        }
    }

    protected GenericConversionService createConversionService() {
        return new DefaultConversionService();
    }

    @Override
    public ConversionService getObject() {
        return conversionService;
    }

    @Override
    public Class<? extends ConversionService> getObjectType() {
        return GenericConversionService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
