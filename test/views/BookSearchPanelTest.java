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
public class BookSearchPanelTest extends UISpecTestCase{

	private Panel panel;
	
	@Before
	public void setUp() throws Exception {
		User user = new User(19, "niel", "121111", "Reiniel Adam", "Lozada", "reiniel_lozada@yahoo.com", "secret", "8194000", "Librarian");
		panel = new Panel(new BookSearchPanel(user));
	}
	
	@Test
	public void initialStateTest(){
		Button searchBtn = panel.getButton("Search");
		assertNotNull(searchBtn);
		assertThat(searchBtn.isEnabled());
		assertThat(searchBtn.isVisible());
		
		Button clearBtn = panel.getButton("Clear");
		assertNotNull(clearBtn);
		assertThat(clearBtn.isEnabled());
		assertThat(clearBtn.isVisible());
		
		TextBox searchField = panel.getTextBox("textSearch");
		assertNotNull(searchField);
		
		Table table = panel.getTable("tableList");
		assertThat(table.isVisible());
	}

}
