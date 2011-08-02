package utilities;

import javax.swing.JOptionPane;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ System.class, CrashHandler.class, JOptionPane.class,
		Exception.class })
public class CrashHandlerTest {

	@Test
	public void test() {
		PowerMock.mockStatic(System.class);
		PowerMock.mockStatic(JOptionPane.class);
		Exception e = PowerMock.createMock(Exception.class);
		CrashHandler.handle(e);
	}

}
