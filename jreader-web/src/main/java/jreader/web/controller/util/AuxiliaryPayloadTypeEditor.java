package jreader.web.controller.util;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

public class AuxiliaryPayloadTypeEditor extends PropertyEditorSupport {
    
    @Override
    public void setAsText(final String text) {
        setValue(AuxiliaryPayloadType.valueOf(text.toUpperCase(Locale.ENGLISH)));
    }

}
