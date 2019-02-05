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
  
  public static void main(String[] args) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;
    try {

      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
      rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");
      rootElement.setAttribute("version", "1.1");
      rootElement.setAttribute("id", "icon");
      rootElement.setAttribute("width", "20px");
      rootElement.setAttribute("height", "20px");
      rootElement.setAttribute("viewBox", "0 0 1000 1000");

      doc.appendChild(rootElement);

      //
      rootElement.appendChild(svgNode(doc, "main", "150", "150", "700", "7000", "0 0 1000 1000", null));
      rootElement.appendChild(svgNode(doc, "ul", "0", "0", "350", "350", "0 0 1000 1000", null));
      rootElement.appendChild(svgNode(doc, "ur", "0", "650", "350", "350", "0 0 1000 1000", null));
      rootElement.appendChild(svgNode(doc, "ll", "650", "0", "350", "350", "0 0 1000 1000", null));
      rootElement.appendChild(svgNode(doc, "lr", "650", "650", "350", "350", "0 0 1000 1000", null));

      // for output to file, console
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      // for pretty print
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(doc);

      // write to console or file
      StreamResult console = new StreamResult(System.out);
      StreamResult file = new StreamResult(new File("emps.xml"));

      // write data
      transformer.transform(source, console);
      transformer.transform(source, file);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Node svgNode(Document doc, String id, String x, String y, String width, String height, String viewBox,
      Node content) {

    Element mainNode = doc.createElement("svg");
    mainNode.setAttribute("id", id);
    mainNode.setAttribute("x", x);
    mainNode.setAttribute("y", y);
    mainNode.setAttribute("width", width);
    mainNode.setAttribute("height", height);
    mainNode.setAttribute("viewBox", viewBox);

    return mainNode;
  }
}