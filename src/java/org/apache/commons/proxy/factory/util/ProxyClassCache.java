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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author James Carman
 * @version 1.0
 */
public class ProxyClassCache
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<ClassLoader, Map<String, WeakReference<Class>>> loaderToClassCache = new WeakHashMap<ClassLoader, Map<String, WeakReference<Class>>>();
    private final ProxyClassGenerator proxyClassGenerator;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ProxyClassCache( ProxyClassGenerator proxyClassGenerator )
    {
        this.proxyClassGenerator = proxyClassGenerator;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public synchronized Class getProxyClass( ClassLoader classLoader, Class... proxyInterfaces )
    {
        final Map<String, WeakReference<Class>> classCache = getClassCache( classLoader );
        final String key = toClassCacheKey( proxyInterfaces );
        Class proxyClass;
        WeakReference<Class> proxyClassReference = classCache.get( key );
        if( proxyClassReference == null )
        {
            proxyClass = proxyClassGenerator.generateProxyClass( classLoader, proxyInterfaces );
            classCache.put( key, new WeakReference<Class>( proxyClass ) );
        }
        else
        {
            synchronized( proxyClassReference )
            {
                proxyClass = proxyClassReference.get();
                if( proxyClass == null )
                {
                    proxyClass = proxyClassGenerator.generateProxyClass( classLoader, proxyInterfaces );
                    classCache.put( key, new WeakReference<Class>( proxyClass ) );
                }
            }
        }
        return proxyClass;
    }

    private Map<String, WeakReference<Class>> getClassCache( ClassLoader classLoader )
    {
        Map<String, WeakReference<Class>> cache = loaderToClassCache.get( classLoader );
        if( cache == null )
        {
            cache = new HashMap<String, WeakReference<Class>>();
            loaderToClassCache.put( classLoader, cache );
        }
        return cache;
    }

    private String toClassCacheKey( Class... proxyInterfaces )
    {
        final StringBuffer sb = new StringBuffer();
        for( int i = 0; i < proxyInterfaces.length; i++ )
        {
            Class proxyInterface = proxyInterfaces[i];
            sb.append( proxyInterface.getName() );
            if( i != proxyInterfaces.length - 1 )
            {
                sb.append( "," );
            }
        }
        return sb.toString();
    }
}
