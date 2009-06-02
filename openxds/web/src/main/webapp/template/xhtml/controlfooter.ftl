<#--


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


-->
<#if parameters.labelposition?default("top") == 'bottom'>
        <p><label <#t/>
    <#if parameters.id?exists>
            for="${parameters.id?html}" <#t/>
    </#if>
    <#if hasFieldErrors>
            class="error"<#t/>
    </#if>
    ><#t/>
        ${parameters.label?html}
    <#if parameters.required?default(false)>
            <span class="req">*</span><#t/>
    </#if>
    <#include "/${parameters.templateDir}/xhtml/tooltip.ftl" />
    </label></p><#t/>
    <#if parameters.labelposition?default("top") == 'top'>
    </div> <#rt/>
    </#if>
    <#if hasFieldErrors>
    <#list fieldErrors[parameters.name] as error>
        <span class="fieldError"><img src="${base}/images/iconWarning.gif" alt="Validation Error" class="icon" /> ${error?html}</span><#lt/>
    </#list>
    </#if>
</#if>
