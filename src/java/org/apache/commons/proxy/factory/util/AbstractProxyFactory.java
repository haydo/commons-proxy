/* $Id$
 *
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.proxy.factory.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.proxy.ObjectProvider;
import org.apache.commons.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;

/**
 * A helpful superclass for {@link org.apache.commons.proxy.ProxyFactory} implementations.
 *
 * @author James Carman
 * @version 1.0
 */
public abstract class AbstractProxyFactory implements ProxyFactory
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    protected Log log;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected AbstractProxyFactory()
    {
        setLog( LogFactory.getLog( getClass() ) );
    }

    public void setLog( Log log )
    {
        this.log = log;
    }

//----------------------------------------------------------------------------------------------------------------------
// ProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    public final Object createDelegatorProxy( ObjectProvider targetProvider, Class... proxyInterfaces )
    {
        return createDelegatorProxy( Thread.currentThread().getContextClassLoader(), targetProvider, proxyInterfaces );
    }

    public final Object createInterceptorProxy( Object target, MethodInterceptor interceptor,
                                                Class... proxyInterfaces )
    {
        return createInterceptorProxy( Thread.currentThread().getContextClassLoader(), target, interceptor,
                                       proxyInterfaces );
    }

    public final Object createInvocationHandlerProxy( InvocationHandler invocationHandler, Class... proxyInterfaces )
    {
        return createInvocationHandlerProxy( Thread.currentThread().getContextClassLoader(), invocationHandler,
                                             proxyInterfaces );
    }
}