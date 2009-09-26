/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/Utility.java,v 1.40 2007/05/18 18:58:59 psterk Exp $
 * ====================================================================
 */
package org.openhealthtools.openxds.repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.openhealthtools.openxds.repository.api.IXdsRepositoryItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Class Declaration.
 * @see
 * @author Farrukh S. Najmi
 * @author Adrian Chong
 * @version   1.2, 05/02/00
 */
public class Utility {
    private static final Log log = LogFactory.getLog(Utility.class);
    
    public static final int MAX_REGISTRY_ID_LENGTH = 256;
    private static HashMap tableNameMap = new HashMap();
    public static final String FILE_SEPARATOR = System.getProperty(
            "file.separator");
    /*
     * See http://www.digit-life.com/articles/ntfs/
     */
    public static final int MAX_FILE_LENGTH = 255;
    /**
     *         128-bit buffer for use with secRand
     */
    private final byte[] secRandBuf16 = new byte[16];

    /**
     * 64-bit buffer for use with secRand
     */
    private final byte[] secRandBuf8 = new byte[8];
    /**
     * random number generator for UUID generation
     */
    private final SecureRandom secRand = new SecureRandom();
    static {
        tableNameMap.put("user", "user_");
        tableNameMap.put("name", "name_");
        tableNameMap.put("classificationscheme", "ClassScheme");
        tableNameMap.put("concept", "ClassificationNode");
    }

    private static HashMap columnNameMap = new HashMap();

    static {
        columnNameMap.put("number", "number_");
        columnNameMap.put("name", "name_");
        columnNameMap.put("user", "user_");
        columnNameMap.put("timestamp", "timestamp_");
    }
    
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */
    
    /* # private Utility _utility; */
    private static Utility instance = null;

    /**
     * Class Constructor.
     *
     *
     * @see
     */
    protected Utility() {
    }
    
    /**
     * Converts an InputStream to a String.
     *
     * @param is the InputStream content to convert to String
     */
    public String unmarshalInputStreamToString(InputStream is)
        throws IOException {

        int buflen = 1024;
        byte[] bytes = new byte[buflen + 1];

        BufferedInputStream bis = new BufferedInputStream(is);

        int bytesRead = 0;

        StringBuffer strBuf = new StringBuffer(); 
        while ((bytesRead = bis.read(bytes, 0, buflen)) != -1) {
            strBuf.append(new String(bytes, 0, bytesRead, "utf-8"));            
        }

        return strBuf.toString();
    }    

    /**
     * Create a temporary file with the specified content.
     *
     * @param content contents of the created RepositoryItem
     * @param deleteOnExit deletes the temporary file upon exiting the VM if true.
     *
     * @return the File instance representing the temporary file
     */
    public static File createTempFile(String content, boolean deleteOnExit) throws IOException {
        // Write to temp file
        byte[] bytes = content.getBytes("utf-8");
        File temp = createTempFile(bytes, "omar", ".txt", deleteOnExit);
        
        return temp;
    }
    
    public static File createTempFile(byte[] bytes, String prefix, String extension, boolean deleteOnExit) throws IOException {
        File temp = File.createTempFile(prefix, extension);
        
        // Delete temp file when program exits.
        if (deleteOnExit) {
            temp.deleteOnExit();
        }
        
        // Write to temp file
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(temp));
        out.write(bytes, 0, bytes.length);
        out.close();
        
        return temp;        
    }
    
    
    /**
     * Create a RepositoryItem containing provided content.
     *
     * @param id the id of the created ExtrinsicObject created for the RepositoryItem
     * @param content contents of the created RepositoryItem
     *
     * @return a <code>RepositoryItem</code> value
     * @throws IOException encountered during IO operations
     */
    public IXdsRepositoryItem createRepositoryItem(String id, String content) throws IOException {
        File file = createTempFile(content, true);
        
        DataHandler dh = new DataHandler(new FileDataSource(file));
        IXdsRepositoryItem ri = new XdsRepositoryItem(id, dh);
        return ri;
    }    

    /**
     * Create a valid registry id.
     */
    public String createId() {
        String id = "urn:uuid:" + getNewUUID();
        return id;
    }
    
    /**
     * Strip urn:uuid: part from start of registry
     * object id.  If id is null or doesn't start with
     * urn:uuid: then the id is returned without modification.
     */
    public String stripId(String id) {
        if (id != null) {
            if (id.startsWith("urn:uuid:")) {
                id = id.substring(9).trim();
            } else {
                // id is urn
                id = id.replaceAll(":", "_");
            }
        }

        return id;
    }

  
    /**
     * Validates URIs according to Registry specs.
     *
     * From Registry specs: "If the URI is a URL then a registry MUST validate the
     * URL to be resolvable at the time of submission before accepting an
     * ExternalLink submission to the registry."
     *
     * Any non-Http URLs and other types of URIs will not be checked. If the http
     * response code is smaller than 200 or bigger than 299, the http URL is
     * considered invalid.
     */
    public boolean isValidURI(String uRI) {
        if (uRI == null) {
            return false;
        }
        
        try {
            java.net.URI _uri = new java.net.URI(uRI);
        } catch (java.net.URISyntaxException e) {
            // Not an URI. return false
            return false;
        }

        // Quoting Doug: "URLs are resolvable URIs"!
        // Try to resolve the URLs here, if it is URL
        try {
            java.net.URL uRL = new java.net.URL(uRI);
            if (uRI.startsWith("http:")) {
                java.net.HttpURLConnection httpUrlConn = (java.net.HttpURLConnection) uRL.openConnection();
                int responseCode = httpUrlConn.getResponseCode();

                if ((responseCode < 200) || (responseCode > 299)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                URLConnection conn = uRL.openConnection();
            }
        } catch (java.net.MalformedURLException e) {
            // Not an URL, will not try to resolve. Valid URI
        } catch (IOException e) {
            return false;
        }
        
        return true;
    }
    
    public static String fixIdentifier(String inputId) {
        String outputId = inputId.replace(':', '_');
        outputId = outputId.replace('.', '_');
        outputId = outputId.replace('$', '_');
        return outputId;
    }
    
    /**
     * Fixes an input URN to comply with URN syntax by replacing invalid chars with '_'
     */
    public static String fixURN(String inputId) {
        String outputId = inputId.replace(FILE_SEPARATOR.charAt( 0 ), ':');
        if (!(outputId.startsWith("urn:"))) {
            outputId = "urn:" + outputId;
        }
        outputId = outputId.replaceAll("[^a-zA-Z_0-9:_]", "_");
        return outputId;
    }
    
    /*
     * Gets the path suitable for the path component of a URL based upon the specified URI.
     */
    public static String getURLPathFromURI(String uri) throws URISyntaxException {
        String path = null;
        
        try {
            URL url = new URL(uri);
            path = url.getPath();
        } catch (MalformedURLException e) {
            //Not a URL. Convert to URN
            String urn = fixURN(uri);
            path = getURLPathFromURN(urn);
        }
        
        return path;
    }
    
    public static String getURLPathFromURN(String urn) throws URISyntaxException {
        String path = null;
        
        URI uri = new java.net.URI(urn);
        path = urn.replaceAll("[:]", "/");
        return path;
    }
    
    public static ZipOutputStream createZipOutputStream(String baseDir, String[] relativeFilePaths, OutputStream os) throws FileNotFoundException, IOException {
        if (baseDir.startsWith("file:/")) {
            baseDir = baseDir.substring(5);
        }
        ZipOutputStream zipoutputstream = new ZipOutputStream(os);

        zipoutputstream.setMethod(ZipOutputStream.STORED);

        for (int i = 0; i < relativeFilePaths.length; i++) {
            File file = new File(baseDir + FILE_SEPARATOR + relativeFilePaths[i]);

            byte [] buffer = new byte [1000];

            int n;

            FileInputStream fis;

            // Calculate the CRC-32 value.  This isn't strictly necessary
            //   for deflated entries, but it doesn't hurt.

            CRC32 crc32 = new CRC32();

            fis = new FileInputStream(file);

            while ((n = fis.read(buffer)) > -1) {
                crc32.update(buffer, 0, n);
            }

            fis.close();

            // Create a zip entry.

            ZipEntry zipEntry = new ZipEntry(relativeFilePaths[i]);

            zipEntry.setSize(file.length());
            zipEntry.setTime(file.lastModified());
            zipEntry.setCrc(crc32.getValue());

            // Add the zip entry and associated data.

            zipoutputstream.putNextEntry(zipEntry);

            fis = new FileInputStream(file);

            while ((n = fis.read(buffer)) > -1) {
                zipoutputstream.write(buffer, 0, n);
            }

            fis.close();

            zipoutputstream.closeEntry();
        }
        
        return zipoutputstream;
    }
    
    /**
     *
     * Extracts Zip file contents relative to baseDir
     * @return An ArrayList containing the File instances for each unzipped file
     */
    public static ArrayList unZip(String baseDir, InputStream is) throws IOException {
        ArrayList files = new ArrayList();
        ZipInputStream zis = new ZipInputStream(is);

        while (true) {
            // Get the next zip entry.  Break out of the loop if there are
            //   no more.
            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry == null) break;

            String entryName = zipEntry.getName();
            if (FILE_SEPARATOR.equalsIgnoreCase("\\")) {                
                // Convert '/' to Windows file separator
                entryName = entryName.replaceAll("/", "\\\\");
            }
            String fileName = baseDir + FILE_SEPARATOR + entryName;
            //Make sure that directory exists.
            String dirName = fileName.substring(0, fileName.lastIndexOf(FILE_SEPARATOR));
            File dir = new File(dirName);
            dir.mkdirs();
            
            //Entry could be a directory
            if (!(zipEntry.isDirectory())) {
                //Entry is a file not a directory.
                //Write out the content of of entry to file 
                File file = new File(fileName);
                files.add(file);
                FileOutputStream fos = new FileOutputStream(file);

                // Read data from the zip entry.  The read() method will return
                //   -1 when there is no more data to read.
                byte [] buffer = new byte [1000];

                int n;

                while ((n = zis.read(buffer)) > -1) {
                    // In real life, you'd probably write the data to a file.
                    fos.write(buffer, 0, n);
                }
                zis.closeEntry();
                fos.close();            
            } else {            
                zis.closeEntry();
            }
        }

        zis.close();
        
        return files;
    }
    
   /* public String mapTableName(RegistryObjectType ro) {
        Class clazz = ro.getClass();
        String className = clazz.getName();
        className = className.substring(className.lastIndexOf(".") + 1);

        if (className.endsWith("Impl")) {
            className = className.substring(0, className.length() - 4);
        }

        return mapTableName(className);
    }*/

    public String mapTableName(String name) {
        String newName = (String) tableNameMap.get(name.toLowerCase().trim());

        if (newName == null) {
            newName = name;
        }

        return newName;
    }

    public String mapColumnName(String name) {
        String newName = (String) columnNameMap.get(name.toLowerCase().trim());

        if (newName == null) {
            newName = name;
        }

        return newName;
    }
    
    public String getClassNameNoPackage(Object obj) {
        
        Class clazz = obj.getClass();
        
        return getClassNameNoPackage(clazz);
    }

    public String getClassNameNoPackage(Class clazz) {        
        String className = clazz.getName();
        
        //Make sure className is not package qualified
        int index = className.lastIndexOf('.');
        if (index >=0 ) {
            className = className.substring(index+1, className.length());
        }
        
        return className;
    }
    
    public static boolean containsWhiteSpacesOnly(String str) {
        boolean whiteSpaceOnly = false;
        String[] strArr = str.split("[^\\s]");
        if ((str == null) || (str.length() == 0) || ((str.split("[^\\s]")).length <= 1)) {
            whiteSpaceOnly = true;
        } 
        
        return whiteSpaceOnly;
    }

    public static String absolutize(String name) {
        // absolutize all the system IDs in the input,
        // so that we can map system IDs to DOM trees.
        try {
            URL baseURL = new File(".").getCanonicalFile().toURL();
            return new URL(baseURL, name).toExternalForm();
        } catch( IOException e ) {
            ; // ignore
        }
        return name;
    }
    
    public static String getFileOrURLName(String fileOrURL) {
        try{
            try {
                return escapeSpace(new URL(fileOrURL).toExternalForm());
            } catch (MalformedURLException e) {
                return new File(fileOrURL).getCanonicalFile().toURL().toExternalForm();
            }
        } catch (Exception e) {
            // try it as an URL
            return fileOrURL;
        }
    }
    
    private static String escapeSpace( String url ) {
        // URLEncoder didn't work.
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            // TODO: not sure if this is the only character that needs to be escaped.
            if (url.charAt(i) == ' ')
                buf.append("%20");
            else
                buf.append(url.charAt(i));
        }
        return buf.toString();
    }
    
    
    /**
     * Method Declaration.
     *
     *
     * @return
     *
     * @see
     */
    public synchronized static Utility getInstance() {
        if (instance == null) {
            instance = new Utility();
        }

        return instance;
    }
    
    
    /**
     * This method is used to retrieve a QName prefix
     *
     * @arg qName
     *  A String containing the QName from which to retrieve the prefix.
     * @return
     *  A String containing the prefix
     */
    public static String getPrefix(String qName) {
        int i = qName.indexOf(':');
        if (i == -1) {
            return null;
        }
        return qName.substring(0, i);
    }
    
    /**
     * This method is used to retrieve the local part of a QName
     *
     * @arg qName
     *  A String containing the QName from which to retrieve the local part.
     * @return
     *  A String containing the local part
     */
    public static String getLocalPart(String qName) {
        int i = qName.lastIndexOf(':');
        if (i == -1) {
            return qName;
        }
        return qName.substring(i + 1);
    }
    
    /**
     * Replaces a JDK 1.5 Pattern.quote method to allow use with JDK 1.4
     *
     * Returns a literal pattern <code>String</code> for the specified
     * <code>String</code>.
     *
     * <p>This method produces a <code>String</code> that can be used to
     * create a <code>Pattern</code> that would match the string
     * <code>s</code> as if it were a literal pattern.</p> Metacharacters
     * or escape sequences in the input sequence will be given no special
     * meaning.
     *
     * @param  s The string to be literalized
     * @return  A literal string replacement
     * @since 1.5
     */
    public static String quote(String s) {
        int slashEIndex = s.indexOf("\\E");
        if (slashEIndex == -1)
            return "\\Q" + s + "\\E";

        StringBuffer sb = new StringBuffer(s.length() * 2);
        sb.append("\\Q");
        slashEIndex = 0;
        int current = 0;
        while ((slashEIndex = s.indexOf("\\E", current)) != -1) {
            sb.append(s.substring(current, slashEIndex));
            current = slashEIndex + 2;
            sb.append("\\E\\\\E\\Q");
        }
        sb.append(s.substring(current, s.length()));
        sb.append("\\E");
        return sb.toString();
    }

    /**
     * This method takes a fileName and verifies that it is a valid filename
     * @param fileName
     * A String that contains the file name to validate
     * @return
     * A String that contains a validated file name
     */
    public static String makeValidFileName(String fileName) {
        // First check to see if fileName is > MAX_FILE_LENGTH
        // If it is, truncate.
        String validFileName = new String(fileName);
        int fileNameLength = validFileName.length();
        if (fileNameLength > MAX_FILE_LENGTH) {
            validFileName = validFileName.substring(0, MAX_FILE_LENGTH-1);
        }
        // Replace illegal characters with a '-'
        validFileName = validFileName.replaceAll("[:*?\"<>]", "-");
        return validFileName;
    }
    
    public UUID getNewUUID() {
        secRand.nextBytes(secRandBuf16);
        secRandBuf16[6] &= 0x0f;
        secRandBuf16[6] |= 0x40; /* version 4 */
        secRandBuf16[8] &= 0x3f;
        secRandBuf16[8] |= 0x80; /* IETF variant */
        secRandBuf16[10] |= 0x80; /* multicast bit */

        long mostSig = 0;

        for (int i = 0; i < 8; i++) {
            mostSig = (mostSig << 8) | (secRandBuf16[i] & 0xff);
        }

        long leastSig = 0;

        for (int i = 8; i < 16; i++) {
            leastSig = (leastSig << 8) | (secRandBuf16[i] & 0xff);
        }

        return new UUID(mostSig, leastSig);
    }
    
}
