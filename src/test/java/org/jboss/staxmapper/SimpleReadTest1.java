/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.staxmapper;

import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import static javax.xml.stream.XMLStreamConstants.*;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class SimpleReadTest1 implements XMLElementReader<Object> {

    public static void main(String[] args) throws XMLStreamException {
        final XMLMapper mapper = XMLMapper.Factory.create();
        mapper.registerRootElement(new QName("urn:test:one", "root"), new SimpleReadTest1());
        mapper.parseDocument(Boolean.TRUE, XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(
                "<root xmlns=\"urn:test:one\">\n" +
                "    <!-- Comment! -->\n" +
                "    <root xmlns=\"urn:test:one\"/>\n" +
                "    <root xmlns=\"urn:test:one\"/>\n" +
                "    <root xmlns=\"urn:test:one\"/>\n" +
                "</root>\n\n"
        )));
    }

    public void readElement(final XMLExtendedStreamReader reader, final Object value) throws XMLStreamException {
        System.out.println("Got my element at " + reader.getLocation());
        while (reader.hasNext()) {
            switch (reader.next()) {
                case COMMENT:
                    System.out.println("Got comment: " + reader.getText());
                    break;
                case END_ELEMENT:
                    return;
                case START_ELEMENT:
                    reader.handleAny(value);
                    break;
            }
        }
    }
}
