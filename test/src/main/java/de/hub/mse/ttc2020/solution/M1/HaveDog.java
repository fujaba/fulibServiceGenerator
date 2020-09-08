package de.hub.mse.ttc2020.solution.M1;

import org.fulib.servicegenerator.FulibPatternDiagram;

import java.util.Objects;

public class HaveDog extends ModelCommand
{

   private static Pattern pattern = null;
   public static final String PROPERTY_name = "name";
   private String name;
   public static final String PROPERTY_age = "age";
   private int age;
   public static final String PROPERTY_owner = "owner";
   private String owner;

   @Override
   public Pattern havePattern() {
      if (pattern == null) {
         pattern = new Pattern();
         PatternObject dog = new PatternObject().setPattern(pattern).setPoId("dog").setHandleObjectClass(Dog.class).setKind("core");
         new PatternAttribute().setObject(dog).setHandleAttrName(Dog.PROPERTY_id).setCommandParamName(HaveDog.PROPERTY_id);
         new PatternAttribute().setObject(dog).setHandleAttrName(Dog.PROPERTY_name).setCommandParamName(HaveDog.PROPERTY_name);
         new PatternAttribute().setObject(dog).setHandleAttrName(Dog.PROPERTY_age).setCommandParamName(HaveDog.PROPERTY_age);

         PatternObject person = new PatternObject().setPattern(pattern).setPoId("person").setHandleObjectClass(Person.class).setKind("context");
         new PatternAttribute().setObject(person).setHandleAttrName(Person.PROPERTY_id).setCommandParamName(HaveDog.PROPERTY_owner);

         new PatternLink().setSource(dog).setHandleLinkName(Dog.PROPERTY_owner).setTarget(person);

         new FulibPatternDiagram().dump("tmp/HaveDogPattern.svg", pattern);
      }
      return pattern;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getOwner());
      return result.toString();
   }

   public String getName()
   {
      return this.name;
   }

   public HaveDog setName(String value)
   {
      this.name = value;
      return this;
   }

   public int getAge()
   {
      return this.age;
   }

   public HaveDog setAge(int value)
   {
      this.age = value;
      return this;
   }

   public String getOwner()
   {
      return this.owner;
   }

   public HaveDog setOwner(String value)
   {
      this.owner = value;
      return this;
   }

}
