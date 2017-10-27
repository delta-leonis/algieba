package io.leonis.algieba.control;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reactor.core.publisher.Flux;

/**
 * The Test PSDControllerConvergenceTest.
 *
 * @author Rimon Oz
 */
@Slf4j
public class PSDControllerConvergenceTest {

  private final static int POINTS = 100;
  private final INDArray setPoint = Nd4j.create(
      new float[]{
          -100f,
          30f
      },
      new int[]{2, 1});

  private final double proportionalFactor = 1.2d;
  private final double summationFactor = 0;
  private final double differenceFactor = 0;
  private final double deltaTime = 0.1d;

  /**
   * Control.
   */
  @Test
  public void control() {
    Flux.fromStream(IntStream.range(1, POINTS).sequential().boxed())
        .scan(new ControlAndResidualState(
                Nd4j.zeros(2, 1),
                Nd4j.zeros(2, 1),
                Lists.newArrayList(
                    Nd4j.zeros(2, 1),
                    Nd4j.zeros(2, 1),
                    Nd4j.zeros(2, 1))),
            (previousState, iteration) -> {
              // compute the new control value
              INDArray newControl = PSDController.apply(
                  previousState.getControlSignal(),
                  Lists.newArrayList(
                      this.setPoint.sub(previousState.getState()),
                      previousState.getResiduals().get(0),
                      previousState.getResiduals().get(1)),
                  this.proportionalFactor,
                  this.summationFactor,
                  this.differenceFactor,
                  this.deltaTime);
              // create the new state, containing the new control signal, residuals, and state
              return new ControlAndResidualState(
                  previousState.getState().add(newControl),
                  newControl,
                  Lists.newArrayList(
                      this.setPoint.sub(previousState.getState()),
                      previousState.getResiduals().get(0),
                      previousState.getResiduals().get(1)));
            })
        .subscribe();

  }

  /**
   * Gets run time.
   *
   * @param testResult the test result
   */
  @AfterMethod
  public void getRunTime(final ITestResult testResult) {
    final long time = testResult.getEndMillis() - testResult.getStartMillis();
    log.info("Iteration #" + testResult.getMethod().getCurrentInvocationCount()
        + " on " + POINTS + " measurements ran in " + time + "ms. "
        + (time / ((double) POINTS)) + "ms per measurement.");
  }

  /**
   * Initialization.
   */
  @BeforeClass
  public void initialization() {
    log.info("Starting PSD controller test suite.");
  }

  /**
   * Terminate.
   */
  @AfterClass
  public void terminate() {
    log.info("Done with PSD controller test suite.");
  }

  @Value
  private class ControlAndResidualState {

    private final INDArray state;
    private final INDArray controlSignal;
    private final List<INDArray> residuals;
  }
}
