package gov.nist.registry.common2.test.io;

import junit.framework.TestCase;
import gov.nist.registry.common2.io.ByteBuffer;

public class ByteBufferTest  extends TestCase {


	public void test_add_get() {
		byte[] data = { 1, 4, 6, 8 };

		ByteBuffer bb = new ByteBuffer();

		bb.append(data, 0, 3);

		assertTrue(bb.size() == 3);

		byte[] ret = bb.get();

		for(int i=0; i<bb.size(); i++ ) {
			assertTrue(data[i] == ret[i]);
		}

		bb = new ByteBuffer();

		bb.append(data, 0, 4);

		assertTrue(bb.size() == 4);

		ret = bb.get();

		for(int i=0; i<bb.size(); i++ ) {
			assertTrue(data[i] == ret[i]);
		}
	}
	
	public void test_add_2() {
		byte[] data = { 1, 4, 6, 8 };
		byte[] data2 = { 3, 5, 7 };

		ByteBuffer bb = new ByteBuffer();

		bb.append(data, 0, data.length);
		bb.append(data2, 0, 3);
		
		assertTrue(bb.size() == 7);
		
		byte[] ret = bb.get();
		
		for(int i=0; i<data.length; i++ ) {
			assertTrue(data[i] == ret[i]);
		}
		
		for (int i=0; i<data2.length; i++) {
			assertTrue(data2[i] == ret[i+data.length]);
		}
		
	}

	public void test_add_3() {
		byte[] data = { 1, 4, 6, 8 };
		byte[] data2 = { 3, 5, 7 };

		ByteBuffer bb = new ByteBuffer(3);

		bb.append(data, 0, data.length);
		bb.append(data2, 0, 3);
		
		assertTrue(bb.size() == 7);
		
		byte[] ret = bb.get();
		
		for(int i=0; i<data.length; i++ ) {
			assertTrue(data[i] == ret[i]);
		}
		
		for (int i=0; i<data2.length; i++) {
			assertTrue(data2[i] == ret[i+data.length]);
		}
		
	}

}
