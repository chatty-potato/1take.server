package potato.onetake.annotation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestTag {

	public static final String UNIT_TEST = "UnitTest";

	public static final String INTEGRATION_TEST = "IntegrationTest";
}
