// This is loaded when gradle is started with -DskipIntegrationTests
// See build.gradle, search for 'spock.configuration'
import de.qaware.mercury.test.plumbing.IntegrationTest

runner {
    exclude {
        annotation IntegrationTest
    }
}