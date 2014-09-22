package ca.etsmtl.applets.etsmobile.http.soap;
//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.0.1.0
//
// Created by Quasar Development at 03-09-2014
//
//---------------------------------------------------


//import static org.ksoap2.serialization.SoapSerializationEnvelope.NIL_LABEL;
//import static org.ksoap2.serialization.SoapSerializationEnvelope.NULL_LABEL;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import ca.etsmtl.applets.etsmobile.model.Enseignant;
import ca.etsmtl.applets.etsmobile.model.Etudiant;
import ca.etsmtl.applets.etsmobile.model.ListeDeCours;
import ca.etsmtl.applets.etsmobile.model.ListeDeSessions;
import ca.etsmtl.applets.etsmobile.model.ListeDesElementsEvaluation;
import ca.etsmtl.applets.etsmobile.model.coursHoraire;
import ca.etsmtl.applets.etsmobile.model.listeCoursHoraire;
import ca.etsmtl.applets.etsmobile.model.listeDesActivitesEtProf;
import ca.etsmtl.applets.etsmobile.model.listeDesCoequipiers;
import ca.etsmtl.applets.etsmobile.model.listeDesProgrammes;
import ca.etsmtl.applets.etsmobile.model.listeHoraireExamensFinaux;
import ca.etsmtl.applets.etsmobile.model.listeJoursRemplaces;
import ca.etsmtl.applets.etsmobile.model.listeSeances;

public class ExtendedSoapSerializationEnvelope extends SoapSerializationEnvelope {
	
	public interface IReferenceObject {
	}
	
	static HashMap<String, Class> classNames = new HashMap<String, Class>();
    static {
        classNames.put("http://etsmtl.ca/^^Etudiant",Etudiant.class);
        classNames.put("http://etsmtl.ca/^^ListeDeCours",ListeDeCours.class);
        classNames.put("http://etsmtl.ca/^^ListeDeSessions",ListeDeSessions.class);
        classNames.put("http://etsmtl.ca/^^listeDesProgrammes",listeDesProgrammes.class);
        classNames.put("http://etsmtl.ca/^^listeDesCoequipiers",listeDesCoequipiers.class);
        classNames.put("http://etsmtl.ca/^^ListeDesElementsEvaluation",ListeDesElementsEvaluation.class);
        classNames.put("http://etsmtl.ca/^^listeDesActivitesEtProf",listeDesActivitesEtProf.class);
        classNames.put("http://etsmtl.ca/^^Enseignant",Enseignant.class);
        classNames.put("http://etsmtl.ca/^^listeHoraireExamensFinaux",listeHoraireExamensFinaux.class);
        classNames.put("http://etsmtl.ca/^^listeCoursHoraire",listeCoursHoraire.class);
        classNames.put("http://etsmtl.ca/^^coursHoraire",coursHoraire.class);
        classNames.put("http://etsmtl.ca/^^listeJoursRemplaces",listeJoursRemplaces.class);
        classNames.put("http://etsmtl.ca/^^listeSeances",listeSeances.class);
    }   

    HashMap<Object, String> reverseReferencesTable = new HashMap<Object, String>();
	HashMap<String, Object> referencesTable = new HashMap<String, Object>();
	private final String MsNs = "http://schemas.microsoft.com/2003/10/Serialization/";
	protected static final int QNAME_NAMESPACE = 0;
	private static final String TYPE_LABEL = "type";

	public ExtendedSoapSerializationEnvelope() {
		super(SoapEnvelope.VER11);
		implicitTypes = true;
		dotNet = true;

		new MarshalGuid().register(this);
		new MarshalDateTime().register(this);
		new MarshalFloat().register(this);
	}

	@Override
	public void writeObjectBody(XmlSerializer writer, KvmSerializable obj) throws IOException {
		if (obj instanceof AttributeContainer) {
			AttributeContainer soapObject = (AttributeContainer) obj;
			int cnt = soapObject.getAttributeCount();
			for (int counter = 0; counter < cnt; counter++) {
				AttributeInfo attributeInfo = new AttributeInfo();
				soapObject.getAttributeInfo(counter, attributeInfo);
				writer.attribute(attributeInfo.getNamespace(), attributeInfo.getName(),
						attributeInfo.getValue() != null ? attributeInfo.getValue().toString() : "");
			}
		}
		super.writeObjectBody(writer, obj);
	}

	@Override
	protected void writeProperty(XmlSerializer writer, Object obj, PropertyInfo type) throws IOException {
		if (obj == null) {
			writer.attribute(xsi, "nil", "true");
			return;
		}

		if (reverseReferencesTable.containsKey(obj)) {
			// this object has been already serialized so use Ref instead
			String id = reverseReferencesTable.get(obj);
			writer.attribute(MsNs, "Ref", id);
			return;
		} else {

			if (obj instanceof IReferenceObject) {
				String id = String.format("i%d", reverseReferencesTable.size() + 1);
				reverseReferencesTable.put(obj, id);
				writer.attribute(MsNs, "Id", id);
			}

			Object[] qName = getInfo(null, obj);
			if (!type.multiRef && qName[2] == null) {
				if (!implicitTypes
						|| (obj.getClass() != type.type && !(obj instanceof Vector) && type.type != String.class)) {
					String xmlName = Helper.getKeyByValue(classNames, obj.getClass());
					if (xmlName != null) {
						String[] parts = xmlName.split("\\^\\^");
						String prefix = writer.getPrefix(parts[0], true);
						writer.attribute(xsi, TYPE_LABEL, prefix + ":" + parts[1]);
					} else {
						String prefix = writer.getPrefix(type.namespace, true);
						writer.attribute(xsi, TYPE_LABEL, prefix + ":" + obj.getClass().getSimpleName());
					}
				}
				// super.writeProperty(writer,obj,type);

				try {
					Method method = this
							.getClass()
							.getSuperclass()
							.getDeclaredMethod("writeElement", XmlSerializer.class, Object.class,
									PropertyInfo.class, Object.class);
					method.setAccessible(true);
					method.invoke(this, writer, obj, type, qName[QNAME_MARSHAL]);
				} catch (NoSuchMethodException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				} catch (IllegalAccessException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				} catch (InvocationTargetException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				}
				// writeElement(writer, obj, type, qName[QNAME_MARSHAL]);
			} else {
				super.writeProperty(writer, obj, type);
			}
		}
	}

	public SoapObject GetExceptionDetail(Element detailElement) {
		Element errorElement = detailElement.getElement(0);
		return GetSoapObject(errorElement);
	}

	public SoapObject GetSoapObject(Element detailElement) {
		try {
			XmlSerializer xmlSerializer = XmlPullParserFactory.newInstance().newSerializer();
			StringWriter writer = new StringWriter();
			xmlSerializer.setOutput(writer);
			detailElement.write(xmlSerializer);
			xmlSerializer.flush();

			XmlPullParser xpp = new KXmlParser();
			xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

			xpp.setInput(new StringReader(writer.toString()));
			xpp.nextTag();
			SoapObject soapObj = new SoapObject(detailElement.getNamespace(), detailElement.getName());
			readSerializable(xpp, soapObj);
			return soapObj;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Object GetHeader(Element detailElement) {
		if (detailElement.getText(0) != null) {
			SoapPrimitive primitive = new SoapPrimitive(detailElement.getNamespace(), detailElement.getName(),
					detailElement.getText(0));
			return primitive;
		}

		return GetSoapObject(detailElement);
	}

	public void Add(String id, Object obj) {
		if (!referencesTable.containsKey(id)) {
			referencesTable.put(id, obj);
		}
	}

	public Object get(AttributeContainer soap, Class cl) {
		if (soap == null) {
			return null;
		}
		try {
			Object refAttr = Helper.getAttribute(soap, "Ref", "http://schemas.microsoft.com/2003/10/Serialization/");
			if (refAttr != null) {
				String ref = (String) refAttr;
				return referencesTable.get(ref);
			} else {

				if (soap instanceof SoapObject) {
					String key = String.format("%s^^%s", ((SoapObject) soap).getNamespace(),
							((SoapObject) soap).getName());
					if (classNames.containsKey(key)) {
						cl = classNames.get(key);
					}
				}
				Constructor ctor = cl.getConstructor(AttributeContainer.class, ExtendedSoapSerializationEnvelope.class);
				return ctor.newInstance(soap, this);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Object get(Object soap, Class cl) {
		if (soap == null) {
			return null;
		}
		try {
			if (soap instanceof Vector) {
				Constructor ctor = cl.getConstructor(Vector.class, ExtendedSoapSerializationEnvelope.class);
				return ctor.newInstance(soap, this);
			}
			Object refAttr = Helper.getAttribute((AttributeContainer) soap, "Ref",
					"http://schemas.microsoft.com/2003/10/Serialization/");
			if (refAttr != null) {
				String ref = (String) refAttr;
				return referencesTable.get(ref);
			} else {
				if (soap instanceof SoapObject) {
					String key = String.format("%s^^%s", ((SoapObject) soap).getNamespace(),
							((SoapObject) soap).getName());
					if (classNames.containsKey(key)) {
						cl = classNames.get(key);
					}
				}
				Constructor ctor = cl.getConstructor(AttributeContainer.class, ExtendedSoapSerializationEnvelope.class);
				return ctor.newInstance(soap, this);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
