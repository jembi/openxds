package gov.nist.registry.common2.testsupport;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.Io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class PatientIdAllocator extends IdAllocator {
	static String current_pid = null; 

	public void loadCurrent() throws XdsInternalException {
		try {
			current_pid =  getPatientIdBase() + "^^^" + getAssigningAuthority();
		} catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}
	}

	public PatientIdAllocator() throws XdsInternalException {
		loadCurrent();
	}

	public PatientIdAllocator(String patient_id) {
		current_pid = patient_id;
	}

	String getAssigningAuthority() throws IOException {
		return Io.stringFromFile(new File(assigning_authority_file)).trim();
	}

	String getPatientIdBase() throws IOException {
		return Io.stringFromFile(new File(patient_id_base_file)).trim();
	}

	String getPatientIdIncr() throws IOException {
		return Io.stringFromFile(new File(patient_id_incr_file)).trim();
	}

	void putPatientIdIncr(String incr) throws IOException {
		PrintStream ps = new PrintStream(new File(patient_id_incr_file));
		ps.print(incr);
		ps.close();
	}

	// by default - one PatientID per execution - NO MORE
	// new spec - always return id in configuration
	public String allocate() throws XdsInternalException {
		try {
			if (current_pid == null) {
				loadCurrent();
			}
			return current_pid;
		} catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}
	}

	public String allocate_new() throws XdsInternalException {
		if (TestConfig.pid_allocate_endpoint != null && !TestConfig.pid_allocate_endpoint.equals("")) {
			try {
				//TODO:
				//current_pid = HttpClient.httpGet(TestConfig.pid_allocate_endpoint);
				TestConfig.rememberPatientId(current_pid);
			} catch (Exception e) {
				throw new XdsInternalException(ExceptionUtil.exception_details(e));
			}
		}
		else 
			System.out.println("WARNING: PID allocation service not configured, using Patient ID coded in test or tool");
		return current_pid;
	}

}
