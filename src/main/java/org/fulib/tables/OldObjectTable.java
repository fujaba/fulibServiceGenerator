package org.fulib.tables;

import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@link Table} specialized for model objects.
 * Using reflection, it provides more functionality for expanding and filtering based on attributes and associations.
 *
 */
public class OldObjectTable extends Table<Object>
{
   private ReflectorMap reflectorMap;

   // =============== Constructors ===============

   @SafeVarargs
   public OldObjectTable(String colName, Object... start)
   {
      super(colName, start);
      this.initReflector(start);
   }

   @SafeVarargs
   private final void initReflector(Object... start)
   {
      if (start.length == 0)
      {
         return;
      }

      Set<String> packageNames = Arrays
         .stream(start)
         .map(o -> o.getClass().getPackage().getName())
         .collect(Collectors.toSet());
      this.reflectorMap = new ReflectorMap(packageNames);
   }

   // =============== Properties ===============

   public ReflectorMap getReflectorMap()
   {
      return this.reflectorMap;
   }

   public void setReflectorMap(ReflectorMap reflectorMap)
   {
      this.reflectorMap = reflectorMap;
   }

   /**
    * Removes all rows where the cell value of the source column does not have the named link to the cell value of the
    * target column in the same row.
    *
    * @param sourceColumn
    *    name of the source column
    * @param linkName
    *    the name of the property on this table's objects
    * @param targetColumn
    *    target columng
    *
    * @return this instance, to allow method chaining
    *
    * @see #filterRows(Predicate)
    */
   public OldObjectTable hasLink(String sourceColumn, String linkName, String targetColumn)
   {
      final int thisColumn = getColumnIndex(sourceColumn);
      final int otherColumn = getColumnIndex(targetColumn);
      this.table.removeIf(row -> {
         final Object source = row.get(thisColumn);
         final Object target = row.get(otherColumn);

         if (!this.reflectorMap.canReflect(source))
         {
            return true;
         }

         final Reflector reflector = this.reflectorMap.getReflector(source);
         final Object linkValue = reflector.getValue(source, linkName);
         final boolean keep =
            linkValue == target || linkValue instanceof Collection && ((Collection<?>) linkValue).contains(target);
         return !keep;
      });
      return this;
   }

   public int getColumnIndex(String columnName) {
      for (int i = 0; i < columns.size(); i++) {
         String name = columns.get(i);
         if (columnName.equals(name)) {
            return i;
         }
      }
      return -1;
   }


   /**
    * Removes all rows where the cell value does not have some link to the cell value of the other table in the
    * same row.
    * This requires this and the other table to share the same underlying data structure.
    * You can also pass this table as {@code otherTable} to check for self-associations.
    * <p>
    * Essentially equivalent to:
    *
    * <pre>{@code
    *    this.filterRows(row -> {
    *       Object source = row.get(this.getColumn());
    *       Object target = row.get(otherTable.getColumn());
    *       for (String linkName : <properties of source>) {
    *          Object linkValue = source.get<linkName>(); // via reflection
    *          if (linkValue == target || linkValue instanceof Collection && ((Collection) linkValue).contains(other)) {
    *             return true;
    *          }
    *       }
    *       return false;
    *    });
    * }</pre>
    *
    * @param sourceColumn
    *    source
    *
    * @param targetColumn
    *    target
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public OldObjectTable hasAnyLink(String sourceColumn, String targetColumn)
   {
      final int thisColumn = this.getColumnIndex(sourceColumn);
      final int otherColumn = getColumnIndex(targetColumn);
      this.table.removeIf(row -> {
         Object start = row.get(thisColumn);
         Object other = row.get(otherColumn);

         if (!this.reflectorMap.canReflect(start))
         {
            return true;
         }

         Reflector reflector = this.reflectorMap.getReflector(start);

         for (final String property : reflector.getAllProperties())
         {
            Object value = reflector.getValue(start, property);
            if (value == other || value instanceof Collection && ((Collection<?>) value).contains(other))
            {
               return false;
            }
         }

         return true;
      });
      return this;
   }

   // --------------- Expansion ---------------

   /**
    * Creates a new column by expanding the given link from the cells of the source column.
    * Links may be simple objects or collections, the latter of which will be flattened.
    * Links that are {@code null} do not create a row.
    * <p>
    * @param sourceColumn
    *    source
    * @param property
    *    the name of the property to expand
    * @param targetColumn
    *    target
    *
    * @return this
    *
    * @see #expandAll(String, Function)
    */
   public OldObjectTable expand(String sourceColumn, String property, String targetColumn)
   {
      setColumnName_(sourceColumn);
      this.expandAllImpl(targetColumn, start -> {
         if (!this.reflectorMap.canReflect(start))
         {
            return Collections.emptySet();
         }

         Reflector reflector = this.reflectorMap.getReflector(start);
         Object value = reflector.getValue(start, property);

         if (value instanceof Collection)
         {
            return (Collection<?>) value;
         }
         else if (value != null)
         {
            return Collections.singleton(value);
         }
         else
         {
            return Collections.emptySet();
         }
      });
      columns.add(targetColumn);
      return this;
   }

   /**
    * Creates a new column by expanding the all links and attributes from the cells of the column this table points to.
    * Links and attributes may be simple objects or collections, the latter of which will be flattened.
    * Links and attributes that are {@code null} do not create a row.
    * <p>
    * Essentially equivalent to:
    *
    * @param newColumnName
    *    the name of the new column
    *
    * @return this
    *
    * @since 1.3
    */
   public OldObjectTable expandAll(String newColumnName)
   {
      this.expandAll(newColumnName, start -> {
         if (!this.reflectorMap.canReflect(start))
         {
            return Collections.emptyList();
         }

         final Reflector reflector = this.reflectorMap.getReflector(start);
         final Collection result = new ArrayList();

         for (final String propertyName : reflector.getAllProperties())
         {
            Object value = reflector.getValue(start, propertyName);
            if (value instanceof Collection)
            {
               result.addAll((Collection) value);
            }
            else if (value != null)
            {
               result.add( value);
            }
         }

         return result;
      });

      return this;
   }

   /**
    * Creates a new column by expanding the given attribute from cells of the column this table points to.
    * <p>
    * Essentially equivalent to:
    * <pre>{@code
    *    this.expand(newColumnName, start -> {
    *       return start.get<attrName>(); // via reflection
    *    });
    * }</pre>
    *
    * @param newColumnName
    *    the name of the new column
    * @param attrName
    *    the name of the attribute to expand
    *
    * @return a table pointing to the new column
    *
    * @see #expand(String, Function)
    * @since 1.2
    */
   public OldObjectTable expandAttribute(String newColumnName, String attrName)
   {
      this.expandAttributeImpl(newColumnName, attrName);
      addColumn(newColumnName);
      return this;
   }




   private void expandAttributeImpl(String newColumnName, String attrName)
   {
      this.expandImpl(newColumnName, start -> {
         if (!this.reflectorMap.canReflect(start))
         {
            return null;
         }

         Reflector reflector = this.reflectorMap.getReflector(start);
         return reflector.getValue(start, attrName);
      });
   }


   @Override
   public OldObjectTable selectColumns(String... columnNames)
   {
      super.selectColumns(columnNames);
      return this;
   }

   @Override
   public OldObjectTable dropColumns(String... columnNames)
   {
      super.dropColumns(columnNames);
      return this;
   }

   @Override
   public OldObjectTable filter(Predicate predicate)
   {
      super.filter(predicate);
      return this;
   }

   // --------------- Overriding Return Type (other) ---------------

   @Override
   public LinkedHashSet toSet()
   {
      return this.stream().collect(Collectors.toCollection(LinkedHashSet::new));
   }

   // =============== Deprecated Members ===============

   /**
    * @param columnName
    *    the name of the column this table should point to
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; set via the constructor and not meant to be changed afterward
    */
   @Deprecated
   public OldObjectTable setColumnName(String columnName)
   {
      this.setColumnName_(columnName);
      return this;
   }

   /**
    * @param table
    *    the list of rows
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public OldObjectTable setTable(ArrayList<ArrayList<Object>> table)
   {
      this.table = new ArrayList<>(table);
      return this;
   }

   /**
    * @param columnMap
    *    the map from column name to index in the lists that make up rows
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public OldObjectTable setColumnMap(LinkedHashMap<String, Integer> columnMap)
   {
      this.setColumnMap_(columnMap);
      return this;
   }

   /**
    * Same as {@link #derive(String, Function)}, except it has stricter requirements on the parameter type of the
    * predicate and does not return a table pointing to the new column.
    *
    * @param columnName
    *    the name of the new column
    * @param function
    *    the function that computes a value for the new column
    *
    * @see #derive(String, Function)
    * @deprecated since 1.2; use {@link #derive(String, Function)} instead
    */
   @Deprecated
   public void addColumn(String columnName, Function<LinkedHashMap<String, Object>, Object> function)
   {
      this.deriveImpl(columnName, function);
   }

   /**
    * Same as {@link #filterRows(Predicate)}, except it has stricter requirements on the parameter type of the
    * predicate.
    *
    * @param predicate
    *    the predicate that determines which rows should be kept
    *
    * @return this table, to allow method chaining
    *
    * @see #filterRows(Predicate)
    * @deprecated since 1.2; use {@link #filterRows(Predicate)} instead
    */
   @Deprecated
   public OldObjectTable filterRow(Predicate<LinkedHashMap<String, Object>> predicate)
   {
      this.filterRowsImpl(predicate);
      return this;
   }
}
