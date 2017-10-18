/*
 * Copyright 2016-2007 Alfonso Marin Lopez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.alfonsomarin.finances.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang.StringUtils.chomp;

/**
 * Reflection utility methods.
 *
 * @author alfonso.marin.lopez
 */
public final class ReflectionUtils {

    private static final ObjectMapper mapper = new ObjectMapper().configure(FAIL_ON_EMPTY_BEANS, false);

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    private ReflectionUtils() {
    }

    /**
     * Returns the value of a field given an {@code Object}.
     *
     * @param resource  Resource containing the field to get the value from.
     * @param fieldName Name of the field to get the value from.
     * @return {@code Optional} containing either the value or empty.
     * @throws NoSuchFieldException the no such field exception
     */
    public static Optional<Object> getFieldValue(Object resource, String fieldName) throws NoSuchFieldException {
        final Class<?> superClass = resource.getClass().getSuperclass();

        Field field = Objects.equals(superClass.getName(), Object.class.getName()) ?
                resource.getClass().getDeclaredField(fieldName) :
                superClass.getDeclaredField(fieldName);

        field.setAccessible(true);
        try {
            return Optional.ofNullable(field.get(resource));
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns the value of a field given an {@code Object} through getter method.
     *
     * @param resource  Resource containing the field to get the value from.
     * @param fieldName Name of the field to get the value from.
     * @return {@code Optional} containing either the value or empty.
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IntrospectionException    the introspection exception
     */
    public static Optional<Object> getFieldValueThroughtGetter(Object resource, String fieldName) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
       
        BeanInfo bean = Introspector.getBeanInfo(resource.getClass());
        for(PropertyDescriptor pd : bean.getPropertyDescriptors()){
            if(pd.getName().equals(fieldName)){
                return Optional.ofNullable(pd.getReadMethod().invoke(resource));
            }
        }
       return Optional.empty();
    }

    /**
     * Set the value of a field given an {@code Object} through setter method.
     *
     * @param resource  Resource containing the field to set the value from.
     * @param fieldName Name of the field to set the value from.
     * @param value The value to set
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IntrospectionException    the introspection exception
     */
    public static void setFieldValueThroughtGetter(Object resource, String fieldName, Object value) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        BeanInfo bean = Introspector.getBeanInfo(resource.getClass());
        for(PropertyDescriptor pd : bean.getPropertyDescriptors()){
            if(pd.getName().equals(fieldName)){
                pd.getWriteMethod().invoke(resource,value);
            }
        }
    }

    /**
     * Given an aspect method signature, gets a Reflection {@code Method}.
     *
     * @param methodSignature Aspect method signature.
     * @return Reflection method.
     * @throws NoSuchMethodException the no such method exception
     */
    public static Method getMethod(MethodSignature methodSignature) throws NoSuchMethodException {
        final Class<?> classType = methodSignature.getDeclaringType();
        return classType.getDeclaredMethod(
                methodSignature.getName(),
                methodSignature.getParameterTypes()
        );
    }

    /**
     * Gets a {@code Method} of a given {@code Class} by name and arguments.
     *
     * @param clazz     Class to get the method from.
     * @param name      Name of the method.
     * @param arguments Arguments of the method.
     * @return The method requested or the first one in case the requested is not found.
     */
    public static Method getMethod(Class<?> clazz, String name, Class[] arguments) {
        try {
            return clazz.getClass().getDeclaredMethod(name, arguments);
        } catch (NoSuchMethodException e) {
            return clazz.getDeclaredMethods()[0];
        }
    }

    /**
     * Unwraps the original object if it is an AOP proxy object.
     *
     * @param bean The object to unwrap.
     * @return Either the unwrapped object or itself.
     */
    public static Object unwrapProxy(Object bean) {
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {
            Advised advised = (Advised) bean;
            try {
                return advised.getTargetSource().getTarget();
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return bean;
    }

    /**
     * Transforms an object array into a String.
     *
     * @param args Object array.
     * @return String representation.
     */
    public static String extractArguments(final Object[] args) {
        StringBuilder buffer = new StringBuilder();
        ofNullable(args)
                .ifPresent(arguments -> buffer
                        .append(
                                chomp(of(arguments)
                                                .filter(arg -> arg != null)
                                                .map(ReflectionUtils::processArg)
                                                .collect(joining(Constants.COMMA)),
                                        Constants.COMMA)
                        )
                );
        return buffer.toString();
    }

    private static String processArg(final Object arg) {
        if (arg instanceof List) {
            return Constants.OPEN_BRACKET +
                    chomp(((List<?>) arg)
                                    .stream()
                                    .map(ReflectionUtils::toString)
                                    .collect(joining(Constants.COMMA)),
                            Constants.COMMA) +
                    Constants.CLOSE_BRACKET;
        }
        return toString(arg);
    }

    private static String toString(final Object arg) {
        try {
            return mapper.writeValueAsString(arg);
        } catch (final JsonProcessingException e) {
            return Constants.NON_PARSEABLE_ARGUMENT;
        }
    }

    /**
     * Returns a stream of classes contained in a given package recursively.
     *
     * @param basePackage Root of he package to scan.
     * @return Stream of classes.
     */
    public static Stream<Class<?>> getClassesInPackage(String basePackage) {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        return provider.findCandidateComponents(basePackage)
                .stream()
                .map(ReflectionUtils::getClass);
    }

    private static Class<?> getClass(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            logger.error("Could not load " + beanDefinition.getBeanClassName(), e);
            return null;
        }
    }
}
