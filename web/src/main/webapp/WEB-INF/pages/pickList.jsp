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
<tr>
    <td>
        <select name="<c:out value="${param.leftId}"/>" multiple="multiple"
            onDblClick="moveSelectedOptions(this,$('<c:out value="${param.rightId}"/>'),true)"
            id="<c:out value="${param.leftId}"/>" size="5">
    <c:if test="${leftList != null}">
        <c:forEach var="list" items="${leftList}" varStatus="status">
            <option value="<c:out value="${list.value}"/>">
                <c:out value="${list.label}" escapeXml="false" />
            </option>
        </c:forEach>
    </c:if>
        </select>
    </td>
    <td class="moveOptions">
        <button name="moveRight" id="moveRight<c:out value="${param.listCount}"/>" type="button" 
            onclick="moveSelectedOptions($('<c:out value="${param.leftId}"/>'),$('<c:out value="${param.rightId}"/>'),true)">
            &gt;&gt;</button><br />
        <button name="moveAllRight" id="moveAllRight<c:out value="${param.listCount}"/>" type="button"
            onclick="moveAllOptions($('<c:out value="${param.leftId}"/>'),$('<c:out value="${param.rightId}"/>'),true)">
            All &gt;&gt;</button><br />
        <button name="moveLeft" id="moveLeft<c:out value="${param.listCount}"/>" type="button"
            onclick="moveSelectedOptions($('<c:out value="${param.rightId}"/>'),$('<c:out value="${param.leftId}"/>'),true)">
            &lt;&lt;</button><br />
        <button name="moveAllLeft" id="moveAllLeft<c:out value="${param.listCount}"/>" type="button"
            onclick="moveAllOptions($('<c:out value="${param.rightId}"/>'),$('<c:out value="${param.leftId}"/>'),true)">
            All &lt;&lt;</button>
    </td>
    <td>
        <select name="<c:out value="${param.rightId}"/>" multiple="multiple"
            id="<c:out value="${param.rightId}"/>" size="5">
    <c:if test="${rightList != null}">
        <c:forEach var="list" items="${rightList}" varStatus="status">
            <option value="<c:out value="${list.value}"/>">
                <c:out value="${list.label}" escapeXml="false"/>
            </option>
        </c:forEach>
    </c:if>
        </select>
    </td>
</tr>