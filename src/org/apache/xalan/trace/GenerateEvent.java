/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xalan" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Lotus
 * Development Corporation., http://www.lotus.com.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.xalan.trace;

import org.xml.sax.*;

import java.util.*;

import java.io.*;

import org.apache.xalan.transformer.TransformerImpl;

/**
 * <meta name="usage" content="advanced"/>
 * Event generated by the XSL processor after it generates a new node in the result tree.
 * This event responds to and is modeled on the SAX events that are sent to the
 * formatter listener FormatterToXXX)classes.
 *
 * @see org.apache.xalan.utils.DOMBuilder
 * @see org.apache.xalan.utils.FormatterToHTML
 * @see org.apache.xalan.utils.FormatterToText
 * @see org.apache.xalan.utils.FormatterToXML
 *
 */
public class GenerateEvent implements java.util.EventListener
{

  /**
   * The XSLT TransformerFactory, which either directly or indirectly contains most needed information.
   * Accessing the m_stylesheetRoot member variable will get you to the stylesheet information.
   *
   * @see org.apache.xalan.xslt.TransformerImpl, org.apache.xalan.xslt.StylesheetRoot,
   *     org.apache.xalan.xslt.Stylesheet
   */
  public TransformerImpl m_processor;

  /**
   * The type of SAX event that was generated, as enumerated in the EVENTTYPE_XXX constants below.
   */
  public int m_eventtype;

  /**
   * Event type generated when a document begins.
   *
   */
  public static final int EVENTTYPE_STARTDOCUMENT = 1;

  /**
   * Event type generated when a document ends.
   */
  public static final int EVENTTYPE_ENDDOCUMENT = 2;

  /**
   * Event type generated when an element begins (after the attributes have been processed but before the children have been added).
   */
  public static final int EVENTTYPE_STARTELEMENT = 3;

  /**
   * Event type generated when an element ends, after it's children have been added.
   */
  public static final int EVENTTYPE_ENDELEMENT = 4;

  /**
   * Event type generated for character data (CDATA and Ignorable Whitespace have their own events).
   */
  public static final int EVENTTYPE_CHARACTERS = 5;

  /**
   * Event type generated for ignorable whitespace (I'm not sure how much this is actually called.
   */
  public static final int EVENTTYPE_IGNORABLEWHITESPACE = 6;

  /**
   * Event type generated for processing instructions.
   */
  public static final int EVENTTYPE_PI = 7;

  /**
   * Event type generated after a comment has been added.
   */
  public static final int EVENTTYPE_COMMENT = 8;

  /**
   * Event type generate after an entity ref is created.
   */
  public static final int EVENTTYPE_ENTITYREF = 9;

  /**
   * Event type generated after CDATA is generated.
   */
  public static final int EVENTTYPE_CDATA = 10;

  /**
   * Character data from a character or cdata event.
   */
  public char m_characters[];

  /**
   * The start position of the current data in m_characters.
   */
  public int m_start;

  /**
   * The length of the current data in m_characters.
   */
  public int m_length;

  /**
   * The name of the element or PI.
   */
  public String m_name;

  /**
   * The string data in the element (comments and PIs).
   */
  public String m_data;

  /**
   * The current attribute list.
   */
  public Attributes m_atts;

  /**
   * Constructor for startDocument, endDocument events.
   *
   * @param processor The XSLT TransformerFactory instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   */
  public GenerateEvent(TransformerImpl processor, int eventType)
  {
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for startElement, endElement events.
   *
   * @param processor The XSLT TransformerFactory Instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param name The name of the element.
   * @param atts The SAX attribute list.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, String name,
                       Attributes atts)
  {

    m_name = name;
    m_atts = atts;
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for characters, cdate events.
   *
   * @param processor The XSLT TransformerFactory instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param ch The char array from the SAX event.
   * @param start The start offset to be used in the char array.
   * @param length The end offset to be used in the chara array.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, char ch[],
                       int start, int length)
  {

    m_characters = ch;
    m_start = start;
    m_length = length;
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for processingInstruction events.
   *
   * @param processor The instance of the XSLT processor.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param name The name of the processing instruction.
   * @param data The processing instruction data.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, String name,
                       String data)
  {

    m_name = name;
    m_data = data;
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for comment and entity ref events.
   *
   * @param processor The XSLT processor instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param data The comment or entity ref data.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, String data)
  {

    m_data = data;
    m_processor = processor;
    m_eventtype = eventType;
  }
}
