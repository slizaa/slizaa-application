package org.slizaa.server.service.svg.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLWriterDOM {

  public static Document read(byte[] bytes) throws Exception {
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    fac.setNamespaceAware(false);
    fac.setValidating(false);
    fac.setFeature("http://xml.org/sax/features/namespaces", false);
    fac.setFeature("http://xml.org/sax/features/validation", false);
    fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    DocumentBuilder builder = fac.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(bytes));
  }

  public static void dump(Node node) throws ParserConfigurationException, TransformerException {
    checkNotNull(node);
    
    // for output to file, console
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    // for pretty print
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(node);

    // write to console or file
    StreamResult console = new StreamResult(System.out);

    // write data
    transformer.transform(source, console);
  }
}