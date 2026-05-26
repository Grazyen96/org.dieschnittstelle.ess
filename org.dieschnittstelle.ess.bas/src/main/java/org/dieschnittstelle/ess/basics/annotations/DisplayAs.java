package org.dieschnittstelle.ess.basics.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//if default used: CLASS, no guaranty it will be available at runtime to be seen through reflection
@Retention(RetentionPolicy.RUNTIME)
//to restrict only to fields
@Target(ElementType.FIELD)
public @interface DisplayAs {
    String value();
}
