/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *
 */
package org.openhealthtools.openxds.registry.patient;

import java.util.List;
import org.openhealthtools.common.utils.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import com.misyshealthcare.connect.net.Identifier;
import static org.junit.Assert.assertEquals;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * This class used to test the Xds patient manager.
 * 
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">raja</a>
 * 
 */
public class XdsRegistryPatientServiceTest {

	private final SecureRandom secRand = new SecureRandom();
	private final byte[] secRandBuf16 = new byte[8];
	XdsRegistryPatientService patientService;
	final static String patientId = IdGenerator.getInstance().createId();
	final static String mergepatienId = IdGenerator.getInstance().createId();

	@Before
	public void setUp() throws Exception {
		patientService = (XdsRegistryPatientService) ModuleManager
				.getXdsRegistryPatientService();
	}

	@Test
	public void testCreatePatient() throws Exception {
		patientService.createPatient(getPatient(patientId), null);
		patientService.createPatient(getPatient(mergepatienId), null);
	}

	@Test
	public void testValidatePatient() throws Exception {
		boolean validPatient = patientService.isValidPatient(
				getPatientIdentifier(patientId), null);
		assertEquals(true, validPatient);
	}

	@Test
	public void testUpdatePatient() throws Exception {
		patientService.updatePatient(getPatient(patientId), null);
	}

	@Test
	public void testMergePatient() throws Exception {
		patientService.mergePatients(getPatient(patientId),
				getPatient(mergepatienId), null);
		System.out.println("successfully merged");

	}

	@Test
	public void testUnMergePatient() throws Exception {
		patientService.unmergePatients(getPatient(patientId),
				getPatient(mergepatienId), null);
		System.out.println("successfully merged");

	}

	private Patient getPatient(String patientId) {
		Patient patient = new Patient();
		List<PatientIdentifier> patientIdentifiers = new ArrayList();
		patientIdentifiers.add(getPatientIdentifier(patientId));
		patient.setPatientIds(patientIdentifiers);
		patient.setDeathIndicator(false);
		return patient;
	}

	private PatientIdentifier getPatientIdentifier(String patientId) {
		PatientIdentifier patientIdentifier = new PatientIdentifier();
		patientIdentifier.setId(patientId);
		patientIdentifier.setAssigningAuthority(new Identifier("IHENA",
				"1.3.6.1.4.1.21367.2009.1.2.300", "ISO"));
		return patientIdentifier;
	}

}
