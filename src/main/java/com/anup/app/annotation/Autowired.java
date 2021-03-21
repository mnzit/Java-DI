package com.anup.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.FIELD,
                ElementType.METHOD,
                ElementType.PARAMETER,
                ElementType.TYPE,
                ElementType.ANNOTATION_TYPE
        }
)
public @interface Autowired {
}
