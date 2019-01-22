package test;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.Asynchronous;
import org.jspresso.framework.application.backend.action.BackendAction;

/**
 * Test asynchronous action.
 *
 * @author Vincent Vandenschrick
 */
@Asynchronous
public class TestAsyncAction extends BackendAction {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    try {
      for (int i = 1; i <= 60; i++) {
        Thread.sleep(10000);
        System.out.println("[" + Thread.currentThread().getName() + "] iteration : " + i + ", HBC = " + getBackendController(context));
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    return true;
  }
}
