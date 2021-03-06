package javaPackagesToJavaDoc.JavaPackages;

import java.util.Objects;
import org.fulib.patterns.*;

public class AddStreamCommand extends ModelCommand
{
   public static final String PROPERTY_incommingRoute = "incommingRoute";
   private String incommingRoute;
   public static final String PROPERTY_outgoingUrl = "outgoingUrl";
   private String outgoingUrl;

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getIncommingRoute());
      result.append(' ').append(this.getOutgoingUrl());
      return result.toString();
   }

   @Override
   public Object run(JavaPackagesEditor editor)
   {
      CommandStream stream = editor.getService().getStream(incommingRoute);
      stream.getTargetUrlList().add(outgoingUrl);
      return null;
   }

   public String getIncommingRoute()
   {
      return this.incommingRoute;
   }

   public AddStreamCommand setIncommingRoute(String value)
   {
      this.incommingRoute = value;
      return this;
   }

   public String getOutgoingUrl()
   {
      return this.outgoingUrl;
   }

   public AddStreamCommand setOutgoingUrl(String value)
   {
      this.outgoingUrl = value;
      return this;
   }

}
