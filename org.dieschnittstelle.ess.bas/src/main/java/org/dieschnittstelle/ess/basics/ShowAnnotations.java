package org.dieschnittstelle.ess.basics;


import org.dieschnittstelle.ess.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.ess.basics.annotations.StockItemProxyImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.dieschnittstelle.ess.basics.reflection.ReflectedStockItemBuilder.getAccessorNameForField;
import static org.dieschnittstelle.ess.utils.Utils.*;

public class ShowAnnotations {

	public static void main(String[] args) {
		// we initialise the collection
		StockItemCollection collection = new StockItemCollection(
				"stockitems_annotations.xml", new AnnotatedStockItemBuilder());
		// we load the contents into the collection
		collection.load();

		for (IStockItem consumable : collection.getStockItems()) {
			showAttributes(((StockItemProxyImpl)consumable).getProxiedObject());
		}

		// we initialise a consumer
		Consumer consumer = new Consumer();
		// ... and let them consume
		consumer.doShopping(collection.getStockItems());
	}

	/*
	 * TODO BAS2
	 */
	private static void showAttributes(Object instance) {
		show("class is: " + instance.getClass());

		try {

			Map<String, String> instanceAttributes = new HashMap<String, String>();
			Map<String, String> instanceAttributes2 = new HashMap<String, String>();

			StringBuilder result= new StringBuilder("{" + instance.getClass().getSimpleName() + " ");

			for (Field field : instance.getClass().getDeclaredFields()) {
//				show("accessibility before: " + field.isAccessible());
				field.setAccessible(true);
//				show("accessibility after: " + field.canAccess(instance));

				instanceAttributes.put(field.getName(), field.get(instance).toString());
			}

			for (Field field : instance.getClass().getDeclaredFields()) {
				String getterName= getAccessorNameForField("get",field.getName());
//				show("getName:" + getterName);
				Method getter = instance.getClass().getDeclaredMethod(getterName);
//				show (" getter: " + getter.invoke(instance));
				result.append(field.getName()).append(":").append(getter.invoke(instance)).append(", ");
				instanceAttributes2.put(field.getName(), getter.invoke(instance).toString());
			}
			result.deleteCharAt(result.length()-2);
			result.append("}");



			show("instance attributes dirty: {" + instance.getClass().getName() + " " + instanceAttributes.keySet() + "}");
			show("instance attributes getter way: " + instance.getClass().getSimpleName() + " " + instanceAttributes2);
//			this is the answer
			show("instance attributes result string: " + result);



			// TODO BAS2: create a string representation of instance by iterating
			//  over the object's attributes / fields as provided by its class
			//  and reading out the attribute values. The string representation
			//  will then be built from the field names and field values.
			//  Note that only read-access to fields via getters or direct access
			//  is required here.

			// TODO BAS3: if the new @DisplayAs annotation is present on a field,
			//  the string representation will not use the field's name, but the name
			//  specified in the the annotation. Regardless of @DisplayAs being present
			//  or not, the field's value will be included in the string representation.

		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("showAnnotations(): exception occurred: " + e,e);
		}

	}

}
