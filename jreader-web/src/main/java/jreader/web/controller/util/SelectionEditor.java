package jreader.web.controller.util;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

import jreader.dao.PostFilter.PostType;

public class SelectionEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) {
        setValue(PostType.valueOf(text.toUpperCase(Locale.ENGLISH)));
    }

}
