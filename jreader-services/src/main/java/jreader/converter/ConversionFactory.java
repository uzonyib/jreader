package jreader.converter;

import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

@Component("conversionService")
public class ConversionFactory implements FactoryBean<ConversionService>, InitializingBean {

    private Set<Converter<?, ?>> converters;

    private GenericConversionService conversionService;

    @Autowired
    public ConversionFactory(final Set<Converter<?, ?>> converters) {
        this.converters = converters;
    }

    @Override
    public void afterPropertiesSet() {
        conversionService = new DefaultConversionService();
        ConversionServiceFactory.registerConverters(converters, conversionService);
        for (Object converter : converters) {
            if (converter instanceof ConversionServiceAware) {
                final ConversionServiceAware c = (ConversionServiceAware) converter;
                c.setConversionService(conversionService);
            }
        }
    }

    @Override
    public ConversionService getObject() {
        return conversionService;
    }

    @Override
    public Class<? extends ConversionService> getObjectType() {
        return ConversionService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
