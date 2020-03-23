package org.fulib.servicegenerator;

import org.junit.Test;
import unikassel.websystem.Shop.ShopEditor;
import unikassel.websystem.Store.StoreEditor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class TestTime
{
   @Test
   public void testGetTime() throws ParseException
   {
      StoreEditor editor = new StoreEditor();

      int hour = 60 * 60 * 1000;
      int hours5 = 5 * hour;
      editor.setTimeDelta(hours5);

      SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      String startTime = editor.getTime();

      Date startDate = isoDateFormat.parse(startTime);
      long millis = startDate.getTime();
      millis += hours5;
      Date plus5Date = new Date(millis);
      String plus5Time = isoDateFormat.format(plus5Date);
      millis += hour;
      Date plus6Date = new Date(millis);
      String plus6Time = isoDateFormat.format(plus6Date);

      editor.setLastTime(plus5Time);

      String plus10Time = editor.getTime();
      String plus15Time = editor.getTime();

      assertThat(plus6Time.compareTo(plus10Time) < 0 , is(true));
      assertThat(plus10Time.compareTo(plus15Time) < 0 , is(true));
   }
}
