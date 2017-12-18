package io.leonis.algieba.spatial;

import java.util.Set;
import lombok.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * The Class AggregatedPotentialField.
 *
 * This class represents an aggregation of multiple {@link PotentialField}.
 *
 * @author Rimon Oz
 */
@AllArgsConstructor
public final class AggregatedPotentialField implements PotentialField {
  @Getter
  private final INDArray origin;
  /**
   * The agregation of {@link PotentialField} as a {@link Set}.
   */
  private final Set<PotentialField> potentialFields;

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPotential(final INDArray positionVector) {
    return this.potentialFields.stream()
        .mapToDouble(potentialField -> potentialField.getPotential(positionVector))
        .sum();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INDArray getForce(final INDArray positionVector) {
    return this.potentialFields.stream()
        .map(potentialField -> potentialField.getForce(positionVector))
        .reduce(INDArray::add)
        .orElse(Nd4j.zeros(positionVector.shape()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getLineIntegral(final INDArray lowerBound, final INDArray upperBound) {
    return this.potentialFields.stream()
        .mapToDouble(potentialField -> potentialField.getLineIntegral(lowerBound, upperBound))
        .sum();
  }
}
