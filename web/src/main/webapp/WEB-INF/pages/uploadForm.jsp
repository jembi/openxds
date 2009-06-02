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
    <title><fmt:message key="upload.title"/></title>
    <meta name="heading" content="<fmt:message key='upload.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<s:form action="uploadFile!upload" enctype="multipart/form-data" method="post" validate="true" id="uploadForm">
    <li class="info">
        <fmt:message key="upload.message"/>
    </li>
    <s:textfield name="name" label="%{getText('uploadForm.name')}" cssClass="text medium" required="true"/>
    <s:file name="file" label="%{getText('uploadForm.file')}" cssClass="text file" required="true"/>
    <li class="buttonBar bottom">
        <s:submit key="button.upload" name="upload" cssClass="button"/>
        <input type="button" value="<fmt:message key="button.cancel"/>" class="button"
            onclick="this.form.onsubmit = null; location.href='mainMenu.html'"/>
    </li>
</s:form>

<script type="text/javascript">
    Form.focusFirstElement($('uploadForm'));
</script>

