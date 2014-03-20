package ca.etsmtl.applets.etsmobile.model;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 2.0.0.4
//
// Created by Quasar Development at 15-01-2014
//
//---------------------------------------------------

import java.util.Hashtable;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import ca.etsmtl.applets.etsmobile.http.soap.ExtendedSoapSerializationEnvelope;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "jours_remplaces")
public class JoursRemplaces extends AttributeContainer implements
		KvmSerializable {

	@DatabaseField(id = true)
	public String dateOrigine;

	@DatabaseField
	public String dateRemplacement;

	@DatabaseField
	public String description;

	public JoursRemplaces() {
	}

	public JoursRemplaces(AttributeContainer inObj,
			ExtendedSoapSerializationEnvelope envelope) {

		if (inObj == null)
			return;

		SoapObject soapObject = (SoapObject) inObj;

		if (soapObject.hasProperty("dateOrigine")) {
			Object obj = soapObject.getProperty("dateOrigine");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				if (j.toString() != null) {
					dateOrigine = j.toString();
				}
			} else if (obj != null && obj instanceof String) {
				dateOrigine = (String) obj;
			}
		}
		if (soapObject.hasProperty("dateRemplacement")) {
			Object obj = soapObject.getProperty("dateRemplacement");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				if (j.toString() != null) {
					dateRemplacement = j.toString();
				}
			} else if (obj != null && obj instanceof String) {
				dateRemplacement = (String) obj;
			}
		}
		if (soapObject.hasProperty("description")) {
			Object obj = soapObject.getProperty("description");
			if (obj != null && obj.getClass().equals(SoapPrimitive.class)) {
				SoapPrimitive j = (SoapPrimitive) obj;
				if (j.toString() != null) {
					description = j.toString();
				}
			} else if (obj != null && obj instanceof String) {
				description = (String) obj;
			}
		}

	}

	@Override
	public Object getProperty(int propertyIndex) {
		if (propertyIndex == 0) {
			return dateOrigine;
		}
		if (propertyIndex == 1) {
			return dateRemplacement;
		}
		if (propertyIndex == 2) {
			return description;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@Override
	public void getPropertyInfo(int propertyIndex,
			@SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		if (propertyIndex == +0) {
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "dateOrigine";
			info.namespace = "http://etsmtl.ca/";
		}
		if (propertyIndex == +1) {
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "dateRemplacement";
			info.namespace = "http://etsmtl.ca/";
		}
		if (propertyIndex == +2) {
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "description";
			info.namespace = "http://etsmtl.ca/";
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
	}

}
