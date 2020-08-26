/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.symbols.testdata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
public @interface AnnotWithDefaults {


    String valueNoDefault();

    String valueWithDefault() default "ddd";

    String[] stringArrayDefault() default {"ddd"};

    String[] stringArrayEmptyDefault() default {};

    MyEnum[] enumArr() default {MyEnum.AA, MyEnum.BB};

    MyEnum enumSimple() default MyEnum.AA;

    enum MyEnum {
        AA, BB, CC
    }

}
