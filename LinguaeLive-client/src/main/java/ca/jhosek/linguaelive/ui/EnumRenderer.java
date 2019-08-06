package ca.jhosek.main.client.ui;

import com.google.gwt.text.shared.AbstractRenderer;

/**
 * Translates enum entries. Use setEmptyValue() if you want to have a custom empty value. Default empty value is "".
 * 
 * @param <T>
 *            an enumeration entry which is to be registered in {@link Translations}
 */

public class EnumRenderer<T extends Enum<?>> extends AbstractRenderer<T> {

  private String emptyValue = "";

  public String render(T object) {
      if (object == null)
          return emptyValue;
      return object.name(); // Translations.translate(object);
  }

  public void setEmptyValue(String emptyValue) {
      this.emptyValue = emptyValue;
  }

}