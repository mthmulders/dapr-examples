package it.mulders.shop.cart.repository;

import java.util.Optional;

import javax.annotation.PreDestroy;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaprStateStore<V> {
    private static final Logger log = LoggerFactory.getLogger(DaprStateStore.class);

    public static class StateEntry {
        public String key;
        public Object value;
        public String etag;

        public StateEntry() {
        }

        public StateEntry(final String key, final Object value) {
            this(key, value, null);
        }

        public StateEntry(final String key, final Object value, final String etag) {
            this.key = key;
            this.value = value;
            this.etag = etag;
        }
    }

    private final Jsonb jsonb;
    private final Client webClient;
    private final String storeName;
    private final Class<V> valueType;

    public DaprStateStore(final String storeName, final Class<V> valueType) {
        this.jsonb = JsonbBuilder.create();
        this.webClient = ClientBuilder.newBuilder()
            .build();
        this.storeName = storeName;
        this.valueType = valueType;
    }

    @PreDestroy
    public void destroy() {
        try {
            this.jsonb.close();
            this.webClient.close();
        } catch (final Exception e) {
            log.error("Error shutting down repository", e);
        }
    }

    public Optional<V> getValue(final String key) {
        log.debug("Retrieving entry from state: store={}, key={}", storeName, key);
        var response = webClient.target("http://localhost:3500/v1.0/state/"+ storeName+ "/" + key)
            .request(MediaType.APPLICATION_JSON)
            .get(Response.class);

        switch (response.getStatus()) {
            case 200: {
                return Optional.of(response.readEntity(valueType));
            }
            case 204: {
                log.debug("Key not found: store={}, key={}", storeName, key);
                return Optional.empty();
            }
            case 400: {
                log.error("Store is missing or misconfigured: store={}", storeName);
                return Optional.empty();
            }
            case 500: {
                log.error("Get state failed: store={}, key={}, message={}", storeName, key, response.getEntity());
                return Optional.empty();
            }
            default: {
                log.error("Unknown error when fetching state: store={}, key={}, status={}", storeName, key, response.getStatus());
                return Optional.empty();
            }
        }
    }

    public void storeValue(final String key, final V value) {
        log.debug("Storing entry to state: store={}, key={}", storeName, key);
        var body = new StateEntry[] {
            new StateEntry(key, value)
        };

        var response = webClient.target("http://localhost:3500/v1.0/state/" + storeName)
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(body));

        if (response.getStatus() != 204) {
            log.error("Could not store value: store={}, key={}, status={}", storeName, key, response.getStatusInfo().getReasonPhrase());
        }
    }
}
