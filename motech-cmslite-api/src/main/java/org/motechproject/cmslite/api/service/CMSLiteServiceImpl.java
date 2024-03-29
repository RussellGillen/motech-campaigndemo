package org.motechproject.cmslite.api.service;


import org.motechproject.cmslite.api.dao.AllStreamContents;
import org.motechproject.cmslite.api.dao.AllStringContents;
import org.motechproject.cmslite.api.dao.BaseContentRepository;
import org.motechproject.cmslite.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;

public class CMSLiteServiceImpl implements CMSLiteService {
    private AllStreamContents allStreamContents;
    private AllStringContents allStringContents;

    @Autowired
    public CMSLiteServiceImpl(AllStreamContents allStreamContents, AllStringContents allStringContents) {
        this.allStreamContents = allStreamContents;
        this.allStringContents = allStringContents;
    }

    @Override
    public StringContent getStringContent(String language, String name) throws ContentNotFoundException {
        return (StringContent) getContent(language, name, allStringContents);
    }

    @Override
    public StreamContent getStreamContent(String language, String name) throws ContentNotFoundException {
        return (StreamContent) getContent(language, name, allStreamContents);
    }

    private Content getContent(String language, String name, BaseContentRepository contentRepository) throws ContentNotFoundException {
        if (language == null || name == null)
            throw new IllegalArgumentException("Language and Name should not be null");
        Content content = contentRepository.getContent(language, name);
        if (content != null) return content;

        throw new ContentNotFoundException();
    }

    @Override
    public void addContent(Content content) throws CMSLiteException {
        if (content == null || content.getLanguage() == null || content.getName() == null) throw new IllegalArgumentException("Content or language or name should not be null");

        if (content instanceof StreamContent)
            allStreamContents.addContent((StreamContent) content);
        else if (content instanceof StringContent)
            allStringContents.addContent((StringContent) content);
    }

}