/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.proxy2.interceptor;

import static org.junit.Assert.assertEquals;

import org.apache.commons.proxy2.Interceptor;
import org.apache.commons.proxy2.Invocation;
import org.apache.commons.proxy2.provider.ObjectProviderUtils;
import org.apache.commons.proxy2.util.AbstractTestCase;
import org.apache.commons.proxy2.util.Echo;
import org.junit.Test;

public class InterceptorUtilsTest extends AbstractTestCase
{
    @Test
    public void testConstant() throws Throwable
    {
        Interceptor interceptor = InterceptorUtils.constant("Hello!");
        Invocation invocation = mockInvocation(Echo.class, "echoBack", String.class).withArguments("World!").build();
        assertEquals("Hello!", interceptor.intercept(invocation));
    }

    @Test
    public void testProvider() throws Throwable
    {
        Interceptor interceptor = InterceptorUtils.provider(ObjectProviderUtils.constant("Foo!"));
        Invocation invocation = mockInvocation(Echo.class, "echoBack", String.class).withArguments("World!").build();
        assertEquals("Foo!", interceptor.intercept(invocation));
    }

    @Test(expected = RuntimeException.class)
    public void testThrowingExceptionObject() throws Throwable
    {
        Interceptor interceptor = InterceptorUtils.throwing(new RuntimeException("Oops!"));
        Invocation invocation = mockInvocation(Echo.class, "echoBack", String.class).withArguments("World!").build();
        interceptor.intercept(invocation);
    }

    @Test(expected = RuntimeException.class)
    public void testThrowingProvidedException() throws Throwable
    {
        Interceptor interceptor = InterceptorUtils
                .throwing(ObjectProviderUtils.constant(new RuntimeException("Oops!")));
        Invocation invocation = mockInvocation(Echo.class, "echoBack", String.class).withArguments("World!").build();
        interceptor.intercept(invocation);
    }

}
