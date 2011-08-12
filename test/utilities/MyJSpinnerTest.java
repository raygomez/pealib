package utilities;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.junit.Test;

public class MyJSpinnerTest {

	@Test
	public void testhasError() {
		MyJSpinner myspinner = new MyJSpinner();

		myspinner.hasError(true);
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				myspinner.getBorder());

	}
	
	@Test
	public void testhasNoError() {
		MyJSpinner myspinner = new MyJSpinner();

		myspinner.hasError(false);
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				myspinner.getBorder());

	}
}
