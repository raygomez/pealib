package utilities;

import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.TextBox;


public class MyTextFieldTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInitialTextField(){
		MyTextField text = new MyTextField();
		
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1,
						Color.gray),
				text.getBorder());
		
		MyTextField text2 = new MyTextField(30);
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1,
						Color.gray),
				text2.getBorder());
	}
	
	@Test
	public void testErrorTextField(){
		MyTextField text = new MyTextField();
		
		text.hasError(true);
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				text.getBorder());
	}
	
}
