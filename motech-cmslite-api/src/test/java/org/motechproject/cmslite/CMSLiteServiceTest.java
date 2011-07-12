package org.motechproject.cmslite;

import org.junit.Test;
import org.motechproject.cmslite.dao.CMSLiteDAO;
import org.motechproject.cmslite.model.Resource;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class CMSLiteServiceTest {

    @Test
    public void shouldReturnInputStreamToContentIfContentExists() throws IOException, ResourceNotFoundException {
        ResourceQuery query = new ResourceQuery("name", "language");
        CMSLiteService cmsLiteService = new CMSLiteService();
        CMSLiteDAO mockDAO = mock(CMSLiteDAO.class);
        Resource resource = new Resource();
        InputStream inputStreamToResource = mock(InputStream.class);

        cmsLiteService.setDAO(mockDAO);
        resource.setInputStream(inputStreamToResource);
        when(mockDAO.getResource(query)).thenReturn(resource);

        InputStream content = cmsLiteService.getContent(query);

        verify(mockDAO).getResource(query);
        assertEquals(inputStreamToResource, content);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionIfContentDoesNotExist() throws ResourceNotFoundException {
        ResourceQuery query = new ResourceQuery("test1", "language");
        CMSLiteService cmsLiteService = new CMSLiteService();
        CMSLiteDAO mockDAO = mock(CMSLiteDAO.class);

        cmsLiteService.setDAO(mockDAO);
        when(mockDAO.getResource(query)).thenReturn(null);

        cmsLiteService.getContent(query);

        fail("Should have thrown ResourceNotFoundException when query is null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenQueryIsNull() throws ResourceNotFoundException {
        CMSLiteService cmsLiteService = new CMSLiteService();
        CMSLiteDAO mockDAO = mock(CMSLiteDAO.class);

        cmsLiteService.setDAO(mockDAO);

        cmsLiteService.getContent(null);
        verify(mockDAO,never()).getResource(null);

        fail("Should have thrown IllegalArgumentException when query is null");
    }

}