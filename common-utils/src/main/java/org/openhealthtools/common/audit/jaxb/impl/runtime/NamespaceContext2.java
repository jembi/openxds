/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
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
 *
 */
package org.openhealthtools.common.audit.jaxb.impl.runtime;

import javax.xml.namespace.NamespaceContext;

/**
 * Maintains namespace&lt;->prefix bindings.
 * 
 * <p>
 * This interface extends {@link NamespaceContext} and provides
 * an additional functionality, which is necessary to declare
 * namespaced attributes on elements. The added method is for
 * self-consumption by the marshaller.
 * 
 * This object is composed into a Serializer.
 */
public interface NamespaceContext2 extends NamespaceContext
{
    /**
     * Declares a new namespace binding within the current context.
     * 
     * <p>
     * The prefix is automatically assigned by MarshallingContext. If
     * a given namespace URI is already declared, nothing happens.
     * 
     * <p>
     * It is <b>NOT</b> an error to declare the same namespace URI
     * more than once.
     * 
     * <p>
     * For marshalling to work correctly, all namespace bindings
     * for an element must be declared between its startElement method and
     * its endAttributes event. Calling the same method with the same
     * parameter between the endAttributes and the endElement returns
     * the same prefix.
     * 
     * @param   requirePrefix
     *      If this parameter is true, this method must assign a prefix
     *      to this namespace, even if it's already bound to the default
     *      namespace. IOW, this method will never return null if this
     *      flag is true. This functionality is necessary to declare
     *      namespace URI used for attribute names.
     * @param   preferedPrefix
     *      If the caller has any particular preference to the
     *      prefix, pass that as a parameter. The callee will try
     *      to honor it. Set null if there's no particular preference.
     * 
     * @return
     *      returns the assigned prefix. If the namespace is bound to
     *      the default namespace, null is returned.
     */
    String declareNamespace( String namespaceUri, String preferedPrefix, boolean requirePrefix );
}
