package test;

import java.util.Map;

import org.jspresso.framework.application.backend.action.persistence.hibernate.ICriteriaRefiner;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;

/**
 * Test criteria refiner.
 *
 * @author Vincent Vandenschrick
 */
public class TestCriteriaRefiner implements ICriteriaRefiner {


  @Override
  public void refineCriteria(EnhancedDetachedCriteria criteria, IQueryComponent queryComponent,
                             Map<String, Object> context) {
    System.out.println("Test criteria refiner reached.");
  }
}
