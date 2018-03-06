/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: SimpleTransform.java,v 1.11 2004/02/17 19:08:36 minchau Exp $
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *  Use the TraX interface to perform a transformation in the simplest manner possible
 *  (3 statements).
 */
public class SimpleTransform
{
	public static void main(String[] args)
    throws TransformerException, TransformerConfigurationException, 
           FileNotFoundException, IOException
  {  
  // Use the static TransformerFactory.newInstance() method to instantiate 
  // a TransformerFactory. The javax.xml.transform.TransformerFactory 
  // system property setting determines the actual class to instantiate --
  // org.apache.xalan.transformer.TransformerImpl.
	TransformerFactory tFactory = TransformerFactory.newInstance();
	
	// Use the TransformerFactory to instantiate a Transformer that will work with  
	// the stylesheet you specify. This method call also processes the stylesheet
  // into a compiled Templates object.
	//Transformer transformer = tFactory.newTransformer(new StreamSource("C:/Zeus/Netbeans/Librerias/xalan-j_2_7_0-bin-2jars/samples/SimpleTransform/birds.xsl"));
	Transformer transformer = tFactory.newTransformer(new StreamSource("C:/Users/zeus/Desktop/cadenaoriginal_3_2.xslt"));
   

	// Use the Transformer to apply the associated Templates object to an XML document
	// (foo.xml) and write the output to a file (foo.out).
	//transformer.transform(new StreamSource("C:/Zeus/Netbeans/Librerias/xalan-j_2_7_0-bin-2jars/samples/SimpleTransform/birds.xml"), new StreamResult(new FileOutputStream("C:/Zeus/Netbeans/Librerias/xalan-j_2_7_0-bin-2jars/samples/SimpleTransform/birds.out")));
	transformer.transform(new StreamSource("C:/Users/zeus/Desktop/XmlSAT4.xml"), new StreamResult(new FileOutputStream("C:/Users/zeus/Desktop/cadena.out")));
	
	System.out.println("************* The result is in cadena.out *************");
  }
}