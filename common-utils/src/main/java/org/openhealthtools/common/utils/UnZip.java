/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
 *    -
 */

package org.openhealthtools.common.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * This utility class defines some methods to extract the jar file.
 * 
 * @author <a href="mailto:rasakannu.palaniyandi@misys.com">Raja</a>
 */
public class UnZip {

  protected ZipFile zippy;

  protected byte[] b;

  public UnZip() {
    b = new byte[8092];
  }

  protected SortedSet dirsMade;

  protected boolean warnedMkDir = false;
  
  /** For a given jar file, process each entry. */
  public void unZip(String fileName,String localAxisRepo) {
	  String[] jarPath = fileName.split("!");
      String exactPath[] = jarPath[0].split("file:/");
	  String exactjarPath = exactPath[1];  
	  dirsMade = new TreeSet();
    try {
      zippy = new ZipFile(exactjarPath);
      Enumeration all = zippy.entries();
      while (all.hasMoreElements()) {
        getFile((ZipEntry) all.nextElement(),localAxisRepo);
      }
    } catch (IOException err) {
      System.err.println("IO Error: " + err);
      return;
    }
  }

  /**
   * Process one file from the jar, given its name.
   * create the file on localAxisRepo.
   */
  protected void getFile(ZipEntry e,String localAxisRepo) throws IOException {
    String zipName = e.getName();
     if (zipName.startsWith("/")) {
        if (!warnedMkDir)
          System.out.println("Ignoring absolute paths");
        warnedMkDir = true;
        zipName = zipName.substring(1);
      }
      if (zipName.endsWith("/")) {
        return;
      }
      // Else must be a file; open the file for output
      // Get the directory part.
      int ix = zipName.lastIndexOf('/');
      if (ix > 0) {
        String dirName = zipName.substring(0, ix);
        if (dirName.startsWith("axis2repository")) {
          String localDir = localAxisRepo + "\\" +dirName;	
          File d = new File(localDir);
          // If it already exists as a dir, don't do anything
          if (!(d.exists() && d.isDirectory())) {
            // Try to create the directory, warn if it fails
            System.out.println("Creating Directory: " + dirName);
            if (!d.mkdirs()) {
              System.err.println("Warning: unable to mkdir "
                  + dirName);
            }
            dirsMade.add(dirName);
          }
      String filePath= localAxisRepo + "\\" +zipName;
      FileOutputStream os = new FileOutputStream(filePath);
      InputStream is = zippy.getInputStream(e);
      int n = 0;
      while ((n = is.read(b)) > 0)
        os.write(b, 0, n);
      is.close();
      os.close();
      }
      }
    }
}
