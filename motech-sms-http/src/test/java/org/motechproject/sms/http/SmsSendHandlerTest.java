package org.motechproject.sms.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.motechproject.sms.api.EventKeys;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class SmsSendHandlerTest {

    private SmsSendHandler handler;
    @Mock
    private HttpClient httpClient;
    @Mock
    private TemplateReader templateReader;

    private SmsSendTemplate template;
    private HttpMethod httpMethod;

    @Before
    public void setUp() {
        initMocks(this);

        template = mock(SmsSendTemplate.class);
        httpMethod = new GetMethod();
        when(template.generateRequestFor(Arrays.asList("0987654321"), "foo bar")).thenReturn(httpMethod);
        when(templateReader.getTemplate(null)).thenReturn(template);
        handler = new SmsSendHandler(templateReader);
        setField(handler, "template", template);
        setField(handler, "httpClient", httpClient);
    }

    @Test
    public void shouldListenToSmsSendEvent() throws NoSuchMethodException {
        Method handleMethod = SmsSendHandler.class.getDeclaredMethod("handle", new Class[]{MotechEvent.class});
        assertTrue(handleMethod.isAnnotationPresent(MotechListener.class));
        MotechListener annotation = handleMethod.getAnnotation(MotechListener.class);
        assertArrayEquals(new String[]{EventKeys.SEND_SMS}, annotation.subjects());
    }

    @Test
    public void shouldMakeRequest() throws IOException {
        MotechEvent event = new MotechEvent(EventKeys.SEND_SMS, new HashMap<String, Object>() {{
            put(EventKeys.RECIPIENTS, Arrays.asList("0987654321"));
            put(EventKeys.MESSAGE, "foo bar");
        }});
        handler.handle(event);

        verify(httpClient).executeMethod(httpMethod);
    }
}
