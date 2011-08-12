package utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.mail.Session;
import javax.mail.Transport;

import models.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.unitils.dbunit.annotation.DataSet;

@DataSet({ "user.xml" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Emailer.class, Transport.class, Session.class })
public class EmailerTest {

	User user;

	@Before
	public void setUp() throws Exception {

		user = new User(1, "raygomez", "Ray", "Gomez",
				"rayvincent.gomez@gmail.com", "USA", "12345678", "User");

	}

	@Test
	public void testSendAcceptedEmail() {
		PowerMock.mockStatic(Transport.class);
		PowerMock.mockStatic(Session.class);
		Emailer.setOn(true);
		assertTrue(Emailer.sendAcceptedEmail(user));
	}

	@Test
	public void testSendAcceptedEmailFail() {
		Emailer.setOn(true);
		assertFalse(Emailer.sendAcceptedEmail(user));
	}

	@Test
	public void testSendRejectEmail() {
		PowerMock.mockStatic(Transport.class);
		PowerMock.mockStatic(Session.class);
		Emailer.setOn(true);
		assertTrue(Emailer.sendRejectEmail(user));
	}

	@Test
	public void testSendRejectEmailFail() {
		Emailer.setOn(true);
		assertFalse(Emailer.sendRejectEmail(user));
	}

	@Test
	public void testSendForgetPasswordEmail() {
		PowerMock.mockStatic(Transport.class);
		PowerMock.mockStatic(Session.class);
		Emailer.setOn(true);
		assertTrue(Emailer.sendForgetPasswordEmail(user));
	}

	@Test
	public void testSendForgetPasswordEmailFail() {
		Emailer.setOn(true);
		assertFalse(Emailer.sendForgetPasswordEmail(user));
	}
}
