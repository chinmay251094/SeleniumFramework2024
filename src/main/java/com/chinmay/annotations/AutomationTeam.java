package com.chinmay.annotations;

import com.chinmay.enums.TestCategory;
import com.chinmay.enums.Tester;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface AutomationTeam {
    public Tester[] author();

    public TestCategory[] category();
}
