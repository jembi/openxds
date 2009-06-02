<%--


     Copyright (C) 2009 SYSNET International <support@sysnetint.com>

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
     implied. See the License for the specific language governing
     permissions and limitations under the License.


--%>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="display.title"/></title>
    <meta name="heading" content="<fmt:message key='display.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<p>Below is a list of attributes that were gathered in UploadAction.java.</p>

<div class="separator"></div>

<table class="detail" cellpadding="5">
    <tr>
        <th>Friendly Name:</th>
        <td><s:property value="name"/></td>
    </tr>
    <tr>
        <th>Filename:</th>
        <td><s:property value="fileFileName"/></td>
    </tr>
    <tr>
        <th>File content type:</th>
        <td><s:property value="fileContentType"/></td>
    </tr>
    <tr>
        <th>File size:</th>
        <td><s:property value="file.length()"/> bytes</td>
    </tr>
    <tr>
        <th class="tallCell">File Location:</th>
        <td>The file has been written to: <br />
            <a href="<c:out value="${link}"/>">
            <c:out value="${location}" escapeXml="false"/></a>
        </td>
    </tr>
    <tr>
        <td></td>
        <td class="buttonBar">
            <input class="button" type="button" value="Done"
                onclick="location.href='mainMenu.html'" />
            <input class="button" type="button" style="width: 120px" value="Upload Another"
                onclick="location.href='uploadFile!default.html'" />
        </td>
    </tr>
</table>



