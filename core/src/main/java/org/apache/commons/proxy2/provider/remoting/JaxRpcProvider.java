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

package org.apache.commons.proxy2.provider.remoting;

import org.apache.commons.proxy2.ObjectProvider;
import org.apache.commons.proxy2.exception.ObjectProviderException;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Returns a proxy2 for a JAX-RPC-based service.
 * <p/>
 * <p>
 * <b>Dependencies</b>:
 * <ul>
 * <li>A JAX-RPC implementation</li>
 * </ul>
 * </p>
 *
 * @author James Carman
 * @since 1.0
 */
public class JaxRpcProvider<T> implements ObjectProvider<T>
{
//**********************************************************************************************************************
// Fields
//**********************************************************************************************************************

    private Class<T> serviceInterface;
    private String wsdlUrl;
    private String serviceNamespaceUri;
    private String serviceLocalPart;
    private String servicePrefix;
    private String portNamespaceUri;
    private String portLocalPart;
    private String portPrefix;

//**********************************************************************************************************************
// Constructors
//**********************************************************************************************************************

    public JaxRpcProvider()
    {
    }

    public JaxRpcProvider( Class<T> serviceInterface )
    {
        this.serviceInterface = serviceInterface;
    }

//**********************************************************************************************************************
// ObjectProvider Implementation
//**********************************************************************************************************************

    public T getObject()
    {
        try
        {
            final Service service = ( wsdlUrl == null ?
                    ServiceFactory.newInstance().createService(getServiceQName()) : ServiceFactory
                    .newInstance().createService(new URL(wsdlUrl), getServiceQName()) );
            final QName portQName = getPortQName();
            return serviceInterface.cast(portQName == null ? service.getPort(serviceInterface) :
                    service.getPort(portQName, serviceInterface));
        }
        catch( ServiceException e )
        {
            throw new ObjectProviderException("Unable to create JAX-RPC service proxy2.", e);
        }
        catch( MalformedURLException e )
        {
            throw new ObjectProviderException("Invalid URL given.", e);
        }
    }

//**********************************************************************************************************************
// Getter/Setter Methods
//**********************************************************************************************************************

    public void setPortLocalPart( String portLocalPart )
    {
        this.portLocalPart = portLocalPart;
    }

    public void setPortNamespaceUri( String portNamespaceUri )
    {
        this.portNamespaceUri = portNamespaceUri;
    }

    public void setPortPrefix( String portPrefix )
    {
        this.portPrefix = portPrefix;
    }

    public void setServiceInterface( Class<T> serviceInterface )
    {
        this.serviceInterface = serviceInterface;
    }

    public void setServiceLocalPart( String serviceLocalPart )
    {
        this.serviceLocalPart = serviceLocalPart;
    }

    public void setServiceNamespaceUri( String serviceNamespaceUri )
    {
        this.serviceNamespaceUri = serviceNamespaceUri;
    }

    public void setServicePrefix( String servicePrefix )
    {
        this.servicePrefix = servicePrefix;
    }

    public void setWsdlUrl( String wsdlUrl )
    {
        this.wsdlUrl = wsdlUrl;
    }

//**********************************************************************************************************************
// Other Methods
//**********************************************************************************************************************

    private QName getPortQName()
    {
        return getQName(portNamespaceUri, portLocalPart, portPrefix);
    }

    private QName getQName( String namespaceUri, String localPart, String prefix )
    {
        if( namespaceUri != null && localPart != null && prefix != null )
        {
            return new QName(namespaceUri, localPart, prefix);
        }
        else if( namespaceUri != null && localPart != null )
        {
            return new QName(namespaceUri, localPart);
        }
        else if( localPart != null )
        {
            return new QName(localPart);
        }
        return null;
    }

    private QName getServiceQName()
    {
        return getQName(serviceNamespaceUri, serviceLocalPart, servicePrefix);
    }
}
