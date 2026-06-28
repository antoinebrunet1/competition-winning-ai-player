package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.AbstractStarModelFactoryTest;
import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;

/**
 * Allows testing for a default StarModelFactory
 * @author Zachary Ethier
 */
public class StarModelFactoryTest extends AbstractStarModelFactoryTest {

  @Override
  public ModelFactory getModelFactory() {
      return new StarModelFactory();
  }
}
