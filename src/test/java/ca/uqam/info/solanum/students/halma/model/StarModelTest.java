package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.AbstractModelTest;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.students.halma.controller.StarModelFactory;

/**
 * Allows testing for a ModelImpl, using a StarModelFactory. There is never more than one Model per baseSize returned by getModel.
 * @author Zachary Ethier
 */
public class StarModelTest extends AbstractModelTest {

  @Override
  public Model getModel(int baseSize) {
    return new StarModelFactory().createModel(baseSize, new String[] {"Max", "Ryan", "Quentin"});
  }

}
