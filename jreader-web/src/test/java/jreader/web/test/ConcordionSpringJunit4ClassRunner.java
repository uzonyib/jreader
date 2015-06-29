package jreader.web.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.concordion.internal.FixtureRunner;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class ConcordionSpringJunit4ClassRunner extends SpringJUnit4ClassRunner {

    private final Description fixtureDescription;
    private final FrameworkMethod fakeMethod;

    public ConcordionSpringJunit4ClassRunner(Class<?> fixtureClass) throws InitializationError, NoSuchMethodException {
        super(fixtureClass);
        String testDescription = ("[Concordion Specification for '" + fixtureClass.getSimpleName()).replaceAll("Test$", "']");
        fixtureDescription = Description.createTestDescription(fixtureClass, testDescription);
        fakeMethod = new FrameworkMethod(this.getClass().getDeclaredMethod("computeTestMethods"));
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return new ArrayList<FrameworkMethod>(Arrays.asList(fakeMethod));
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, final Object test) {
        return new Statement() {
            public void evaluate() throws Throwable {
                new FixtureRunner().run(test);
            }
        };
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        return fixtureDescription;
    }

}
