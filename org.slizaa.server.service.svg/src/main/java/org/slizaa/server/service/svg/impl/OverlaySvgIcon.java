package org.slizaa.server.service.svg.impl;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OverlaySvgIcon {

  public NodeList _mainNodes;

  public NodeList _ulNodes;

  public NodeList _urNodes;

  public NodeList _llNodes;

  public NodeList _lrNodes;

  public String create() {

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
      Node mainNode = rootElement.appendChild(svgNode(doc, "main", "150", "150", "700", "700", "0 0 1000 1000", null));
      addAsChildNodes(mainNode, _mainNodes);
      
      Node urNode = rootElement.appendChild(svgNode(doc, "ur", "650", "0", "350", "350", "0 0 1000 1000", null));
      addAsChildNodes(urNode, _urNodes);
      
      Node lrNode = rootElement.appendChild(svgNode(doc, "lr", "650", "650", "350", "350", "0 0 1000 1000", null));
      addAsChildNodes(lrNode, _lrNodes);
      
      Node llNode = rootElement.appendChild(svgNode(doc, "ll", "0", "650", "350", "350", "0 0 1000 1000", null));
      addAsChildNodes(llNode, _llNodes);   
      
      Node ulNode = rootElement.appendChild(svgNode(doc, "ul", "0", "0", "350", "350", "0 0 1000 1000", null));
      addAsChildNodes(ulNode, _ulNodes);

      return toString(doc);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void addAsChildNodes(Node mainNode, NodeList nodeList) {
    if (nodeList != null) {
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Node newNode = mainNode.getOwnerDocument().importNode(node, true);
          mainNode.appendChild(newNode);
        }
      } 
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

  private static String toString(Document document) throws TransformerException {

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.INDENT, "no");
    StringWriter writer = new StringWriter();
    transformer.transform(new DOMSource(document), new StreamResult(writer));
    return writer.toString();

  }
}
