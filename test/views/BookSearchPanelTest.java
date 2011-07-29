package views;

import models.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookSearchPanelTest extends UISpecTestCase {

	private Panel panelLibrarian;
	private Panel panelUser;

	@Before
	public void setUp() throws Exception {
		User userLibrarian = new User(19, "niel", "121111", "Reiniel Adam", "Lozada",
				"reiniel_lozada@yahoo.com", "secret", "8194000", "Librarian");
		User userOrdinary = new User(20, "niel", "121111", "Reiniel Adam", "Lozada",
				"reiniel_lozada@yahoo.com", "secret", "8194000", "User");
		panelLibrarian = new Panel(new BookSearchPanel(userLibrarian));
		panelUser = new Panel(new BookSearchPanel(userOrdinary));
	}

	@Test
	public void initialStateTestLibrarian() {
		Button button;
		String[] buttonArray = new String[] { "Search", "Clear", "Add Book" };

		for (String s : buttonArray) {
			button = panelLibrarian.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

		TextBox searchField = panelLibrarian.getTextBox("textSearch");
		assertNotNull(searchField);

		Table table = panelLibrarian.getTable("tableList");
		assertThat(table.isVisible());
	}
	
	@Test
	public void initialStateTestUser(){
		Button button;
		String[] buttonArray = new String[] { "Search", "Clear" };

		for (String s : buttonArray) {
			button = panelUser.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

		TextBox searchField = panelUser.getTextBox("textSearch");
		assertNotNull(searchField);

		Table table = panelUser.getTable("tableList");
		assertThat(table.isVisible());
	}

}
