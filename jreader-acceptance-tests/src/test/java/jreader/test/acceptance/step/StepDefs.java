package jreader.test.acceptance.step;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import jreader.test.acceptance.util.CucumberTestConfiguration;

@ContextConfiguration(classes = CucumberTestConfiguration.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
abstract class StepDefs {

}
