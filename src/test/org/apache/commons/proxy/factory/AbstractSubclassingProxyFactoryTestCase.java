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
package org.apache.commons.proxy.factory;

import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.handler.NullInvocationHandler;
import org.apache.commons.proxy.provider.ConstantProvider;
import org.apache.commons.proxy.util.Echo;
import org.apache.commons.proxy.util.EchoImpl;

/**
 * @author James Carman
 * @version 1.0
 */
public abstract class AbstractSubclassingProxyFactoryTestCase extends AbstractProxyFactoryTestCase
{
    protected AbstractSubclassingProxyFactoryTestCase( ProxyFactory factory )
    {
        super( factory );
    }

    public void testCanProxy()
    {
        assertTrue( factory.canProxy( Echo.class ) );
        assertTrue( factory.canProxy( EchoImpl.class ) );
    }

    public void testDelegatorWithSuperclass()
    {
        final Echo echo = ( Echo )factory.createDelegatorProxy( new ConstantProvider( new EchoImpl() ), Echo.class, EchoImpl.class );
        assertTrue( echo instanceof EchoImpl );
    }

    public void testInterceptorWithSuperclass()
    {
        final Echo echo = ( Echo )factory.createInterceptorProxy( new EchoImpl(), new NoOpMethodInterceptor(), Echo.class, EchoImpl.class );
        assertTrue( echo instanceof EchoImpl );
    }

    public void testInvocationHandlerWithSuperclass()
    {
        final Echo echo = ( Echo )factory.createInvocationHandlerProxy( new NullInvocationHandler(), Echo.class, EchoImpl.class );
        assertTrue( echo instanceof EchoImpl );
    }
}
