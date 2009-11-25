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
OpenXDS_2010_Keystore.p12 - keystore with root certs and signed master key
OpenXDS_2010_Truststore.jsk - truststore for holding CA cert that signed all other certs 

Connectathon 2010 (CA signed certificates)
==========================================
#generate keystore:
openssl pkcs12 -export -out OpenXDS_2010_Keystore.p12 -in certs/XDSb_REG_MISYS.ihe.net.pem -inkey requests/XDSb_REG_MISYS.ihe.net.pem

#generate truststore:
keytool -import -v -trustcacerts -file demoCA\cacert.pem -keystore OpenXDS_2010_Truststore.jks -storepass password -alias mir

# import my cert into truststore
keytool -importkeystore -srckeystore OpenXDS_2010_Keystore.p12 -destkeystore OpenXDS_2010_Truststore.jks -srcstoretype PKCS12 -deststoretype JKS -srcstorepass password -deststorepass password

#list results
keytool -list -v -keystore OpenXDS_2010_Truststore.jks -storepass password
