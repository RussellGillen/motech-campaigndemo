/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2011 Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, nor its respective contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA AND ITS CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION USA OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package org.motechproject.server.appointmentreminder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.appointmentreminder.dao.PatientDAO;
import org.motechproject.appointmentreminder.model.Appointment;
import org.motechproject.appointmentreminder.model.Patient;
import org.motechproject.appointmentreminder.model.Preferences;
import org.motechproject.context.Context;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.SchedulableJob;
import org.motechproject.server.event.EventListener;
import org.motechproject.server.gateway.MotechSchedulerGateway;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UncheduleAppointmentReminderTest {
	@InjectMocks
	private EventListener unscheduleAppointmentReminderHandler = new UnscheduleAppointmentReminderHandler();
	@Mock
	private Context context;
	@Mock
	private PatientDAO patientDAO;
	@Mock
	private MotechSchedulerGateway motechSchedulerGateway;
	
	static final String APPT_ID = "000111";
	static final String PAT_ID = "0001";
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testHandleHappyPath() {
		MotechEvent event = new MotechEvent("", "", null);
		event.getParameters().put(EventKeys.APPOINTMENT_ID_KEY, APPT_ID);
		event.getParameters().put(EventKeys.PATIENT_ID_KEY, PAT_ID);

        String reminderScheduledJobId = "999";
		
		Patient p = new Patient();
		p.setPreferences(new Preferences());
		p.getPreferences().setBestTimeToCallHour(10);
		p.getPreferences().setBestTimeToCallMinute(10);
	
		Appointment a = new Appointment();
		a.setReminderWindowEnd(new Date());
		a.setReminderWindowStart(new Date());
        a.setReminderScheduledJobId(reminderScheduledJobId);
		
		// stub
		when(patientDAO.get(PAT_ID)).thenReturn(p);
		when(patientDAO.getAppointment(APPT_ID)).thenReturn(a);
		when(context.getMotechSchedulerGateway()).thenReturn(motechSchedulerGateway);

		// run
		unscheduleAppointmentReminderHandler.handle(event);
		
		// verify
		verify(patientDAO).get(PAT_ID);
		verify(patientDAO).getAppointment(APPT_ID);
		verify(context).getMotechSchedulerGateway();
		verify(motechSchedulerGateway).unscheduleJob(reminderScheduledJobId);
	}

    @Test
    public void testHandle_InvalidAppointmentIdType() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(EventKeys.APPOINTMENT_ID_KEY, new Integer(0));
		params.put(EventKeys.PATIENT_ID_KEY, PAT_ID);

        MotechEvent motechEvent = new MotechEvent("", "", params);

        unscheduleAppointmentReminderHandler.handle(motechEvent);

        verify(patientDAO, times(0)).get(anyString());
    }

    @Test
    public void testHandle_NoAppointmentId() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
		params.put(EventKeys.PATIENT_ID_KEY, PAT_ID);

        MotechEvent motechEvent = new MotechEvent("", "", params);

        unscheduleAppointmentReminderHandler.handle(motechEvent);

        verify(patientDAO, times(0)).get(anyString());
    }

    @Test
    public void testHandle_InvalidPatientIdType() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(EventKeys.APPOINTMENT_ID_KEY, "foo");
		params.put(EventKeys.PATIENT_ID_KEY, new Integer(0));

        MotechEvent motechEvent = new MotechEvent("", "", params);

        unscheduleAppointmentReminderHandler.handle(motechEvent);

        verify(patientDAO, times(0)).get(anyString());
    }

    @Test
    public void testHandle_NoPatientId() throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(EventKeys.APPOINTMENT_ID_KEY, "foo");

        MotechEvent motechEvent = new MotechEvent("", "", params);

        unscheduleAppointmentReminderHandler.handle(motechEvent);

        verify(patientDAO, times(0)).get(anyString());
    }
}