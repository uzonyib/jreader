package jreader.web.controller.util;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

import jreader.dao.FeedEntryFilter.Selection;

public class SelectionEditor extends PropertyEditorSupport {
    
    @Override
    public void setAsText(final String text) {
        setValue(Selection.valueOf(text.toUpperCase(Locale.ENGLISH)));
    }

}
