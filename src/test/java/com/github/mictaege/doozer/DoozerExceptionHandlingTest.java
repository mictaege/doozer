package com.github.mictaege.doozer;

import com.github.mictaege.lenientfun.FunctionalRuntimeException;
import org.junit.Test;

import static com.github.mictaege.doozer.Doozer.makeA;
import static com.github.mictaege.doozer.FaultyPerson.Fields.nickName;

public class DoozerExceptionHandlingTest {

    @Test(expected = FunctionalRuntimeException.class)
    public void shouldHandleExceptionsDuringObjectCreation() {
        makeA(FaultyPerson::new);
    }

    @Test(expected = FunctionalRuntimeException.class)
    public void shouldHandleExceptionsDuringObjectModificationn() {
        makeA(() -> new FaultyPerson("Sarah", "Klein"),
                p -> p.apply(FaultyPerson::setNickName, "Sa"));
    }

    @Test(expected = FunctionalRuntimeException.class)
    public void shouldHandleExceptionsIfFieldIsMissing() {
        makeA(() -> new FaultyPerson("Sarah", "Klein"),
                p -> p.with(nickName, "Sa"));
    }

}