/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */
README.txt - This file.
keystore.key - initial unsigned generated key, saved and never touched.  In case something goes FUBAR.
EHR_MISYS.key - copy of key, for uniform naming.  Used to generate csr.
EHR_MISYS.csr - signing request.  Sent to CA.
EHR_MISYS.cer - signed cert made by CA from .csr.  Recieved from CA.
EHR_MISYS_KEY.key - Keystore with root certs and signed master key.
Identrus_Test_Root.cer - Root cert.
Wells_CA.cer - Intermediate cert.
EHR_MISYS_TRUST.key - keystore for holding cert that signed all other certs.

EMR_MISYS_08_KEY.p12 - self-signed certificate keystore
EMR_MISYS_08_TRUST.jsk - truststore 

to generate key:
openssl pkcs12 -export -out keystore.pkcs12 -in test_sys_1.cert.pem -inkey test_sys_1.key.pem
openssl pkcs12 -export -out OpenPIXPDQ_2009_KEY.pkcs12 -in PAT_IDENTITY_X_REF_MGR_MISYSPLC.cert.pem -inkey PAT_IDENTITY_X_REF_MGR_MISYSPLC.key.pem

to convert p12 key to jks key (need to have a jetty installation):
java -cp c:\tools\jetty\jetty-6.1.14\lib\jetty-6.1.14.jar org.mortbay.jetty.security.PKCS12Import OpenPIXPDQ_2009_KEY.pkcs12 OpenPIXPDQ_2009_KEY.jks

to generate truststore:
keytool -import -alias mesa -file mesa.cert -keystore TrustStore
keytool -import -alias xxx -file xxx.cert -keystore OpenPIXPDQ_2009_STORE.jks

