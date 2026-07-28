package dev.kaldiroglu.dp.structural.proxy.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * A protection proxy earns its place by what it stops. These tests count what reached the
 * gateway, not what was printed.
 */
class NetworkProxyTest {

    private Gateway gateway;
    private Network network;

    @BeforeEach
    void setUp() {
        gateway = Gateway.getInstance();
        gateway.resetCounts();
        Logger.clear();
        network = new ProxyServer(gateway);
    }

    @Test
    @DisplayName("a permitted request reaches the gateway")
    void permitted() throws Exception {
        network.ftp("10.0.0.2", "202.168.2.200");

        assertEquals(1, gateway.ftpCalls());
    }

    @Test
    @DisplayName("ftp to a 192 address is refused, and never reaches the gateway")
    void ftpRuleStopsIt() {
        ForbiddenAccessException thrown = assertThrows(ForbiddenAccessException.class,
                () -> network.ftp("10.0.0.2", "192.168.2.200"));

        assertTrue(thrown.getMessage().contains("not permitted"));
        assertEquals(0, gateway.ftpCalls(), "the gateway was never called");
    }

    @Test
    @DisplayName("telnet from a 10 address is refused, and never reaches the gateway")
    void telnetRuleStopsIt() {
        assertThrows(ForbiddenAccessException.class,
                () -> network.telnet("10.0.0.2", "88.168.2.200"));

        assertEquals(0, gateway.telnetCalls());
    }

    @Test
    @DisplayName("a refused request is still logged — the proxy saw it")
    void refusedRequestsAreLogged() {
        assertThrows(ForbiddenAccessException.class,
                () -> network.ftp("10.0.0.2", "192.168.2.200"));

        assertEquals(1, Logger.entries().size());
        assertTrue(Logger.entries().getFirst().contains("wants to ftp"));
    }

    @Test
    @DisplayName("the gateway holds no access rule of its own")
    void theRealSubjectKnowsNothing() throws Exception {
        // Straight at the gateway, the forbidden request succeeds — proof that the rule
        // lives in the proxy and only in the proxy.
        gateway.ftp("10.0.0.2", "192.168.2.200");

        assertEquals(1, gateway.ftpCalls());
    }

    @Test
    @DisplayName("clients are handed the proxy, never the gateway")
    void theServerHandsOutTheProxy() {
        Network handed = NetworkServer.getInstance().getNetwork();

        assertTrue(handed instanceof ProxyServer);
        assertTrue(Network.class.isAssignableFrom(ProxyServer.class));
        assertTrue(Network.class.isAssignableFrom(Gateway.class));
    }

    @Test
    @DisplayName("nothing in this package is named in Turkish any more")
    void identifiersAreEnglish() {
        // It was YasakKardesimException, and its messages were Turkish too.
        assertEquals("ForbiddenAccessException", ForbiddenAccessException.class.getSimpleName());
    }
}
