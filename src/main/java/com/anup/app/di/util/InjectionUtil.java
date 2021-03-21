package com.anup.app.di.util;

import com.anup.app.annotation.Autowired;
import com.anup.app.annotation.Qualifer;
import com.anup.app.di.Injector;
import org.burningwave.core.classes.FieldCriteria;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.burningwave.core.assembler.StaticComponentContainer.Fields;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
public class InjectionUtil {

    private InjectionUtil() {
    }

    /**
     * Perform injection recursively, for each service inside the Client class
     *
     * @param injector
     * @param classz
     * @param classInstance
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void autowire(Injector injector, Class<?> classz, Object classInstance)
            throws
            InstantiationException,
            IllegalAccessException {
        Collection<Field> fields = Fields
                .findAllAndMakeThemAccessible(
                        FieldCriteria
                                .forEntireClassHierarchy()
                                .allThoseThatMatch(
                                        field -> field.isAnnotationPresent(Autowired.class)
                                ),
                        classz
                );

        for (Field field : fields) {
            String qualifer = field.isAnnotationPresent(Qualifer.class) ? field.getAnnotation(Qualifer.class).value() : null;
            Object fieldInstance = injector.getBeanInstance(field.getType(), field.getName(), qualifer);
            Fields.setDirect(classInstance, field, fieldInstance);
            autowire(injector, fieldInstance.getClass(), fieldInstance);
        }
    }
}
