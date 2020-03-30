package de.qaware.mercury.test.plumbing;

import org.springframework.test.context.ActiveProfilesResolver;

/**
 * Set MERCURY_NO_TESTCONTAINER=1 to disable testcontainer. If that variable is not set or not 1,
 * testcontainers will be used.
 * <p>
 * The way that works is by 2 different profiles:
 * <p>
 * - with testcontainer (see application-testcontainer.yaml),
 * - without testcontainer (see application-no-testcontainer.yaml)
 * <p>
 * The profile 'test' is enabled in both cases.
 */
public class CustomActiveProfileResolver implements ActiveProfilesResolver {
    public static final String ENV_VARIABLE = "MERCURY_NO_TESTCONTAINER";

    @Override
    public String[] resolve(Class<?> testClass) {
        String env = System.getenv(ENV_VARIABLE);

        if ("1".equals(env)) {
            return new String[]{"test", "no-testcontainer"};
        }

        return new String[]{"test", "testcontainer"};
    }
}
