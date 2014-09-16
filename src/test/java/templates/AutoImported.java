package templates;

import java.util.List;

import br.com.caelum.vraptor.panettone.User;

public class AutoImported {

	public String render(List<User> users) {
		return users == null ? "empty" : users.size() + "";
	}

}
