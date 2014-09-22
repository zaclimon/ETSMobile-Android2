package ca.etsmtl.applets.etsmobile.model;

//----------------------------------------------------
//
//Generated by www.easywsdl.com
//Version: 4.0.1.0
//
//Created by Quasar Development at 03-09-2014
//
//---------------------------------------------------


import java.util.Hashtable;

import org.ksoap2.serialization.AttributeContainer;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import ca.etsmtl.applets.etsmobile.http.soap.ExtendedSoapSerializationEnvelope;

public class listeHoraireExamensFinaux extends DonneesRetournees implements KvmSerializable
{
  
  public ArrayOfHoraireExamenFinal listeHoraire=new ArrayOfHoraireExamenFinal();

  public listeHoraireExamensFinaux ()
  {
  }

  public listeHoraireExamensFinaux (AttributeContainer inObj,ExtendedSoapSerializationEnvelope envelope)
  {
	    super(inObj, envelope);
	    if (inObj == null)
          return;


      SoapObject soapObject=(SoapObject)inObj;  
      if (soapObject.hasProperty("listeHoraire"))
      {	
	        SoapObject j = (SoapObject) soapObject.getProperty("listeHoraire");
	        this.listeHoraire = new ArrayOfHoraireExamenFinal(j,envelope);
      }


  }

  @Override
  public Object getProperty(int propertyIndex) {
      int count = super.getPropertyCount();
      //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
      //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
      if(propertyIndex==count+0)
      {
          return listeHoraire;
      }
      return super.getProperty(propertyIndex);
  }


  @Override
  public int getPropertyCount() {
      return super.getPropertyCount()+1;
  }

  @Override
  public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
  {
      int count = super.getPropertyCount();
      if(propertyIndex==count+0)
      {
          info.type = PropertyInfo.VECTOR_CLASS;
          info.name = "listeHoraire";
          info.namespace= "http://etsmtl.ca/";
      }
      super.getPropertyInfo(propertyIndex,arg1,info);
  }
  
  @Override
  public void setProperty(int arg0, Object arg1)
  {
  }

}