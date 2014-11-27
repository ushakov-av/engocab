package ru.mipt.engocab;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Alexander V. Ushakov
 */
@Ignore
public class TestUtils {

    private TestUtils() { }

    /**
     * @return mockery with ClassImposteriser
     */
    public static Mockery newMockery() {
        Mockery mockery = new Mockery();
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        return mockery;
    }
}
