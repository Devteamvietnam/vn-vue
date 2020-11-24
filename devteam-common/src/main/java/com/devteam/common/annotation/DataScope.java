package com.devteam.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Data permission filter annotation
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{
	/**
     * The alias of the department table
     */
    public String deptAlias() default "";

    /**
     * The alias of the user table
     */
    public String userAlias() default "";
}
