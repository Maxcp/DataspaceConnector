package io.dataspaceconnector.model;

import io.dataspaceconnector.utils.ErrorMessages;
import io.dataspaceconnector.utils.MetadataUtils;
import io.dataspaceconnector.utils.Utils;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;

/**
 * Creates and updates a broker.
 */
@Component
public class BrokerFactory implements AbstractFactory<Broker, BrokerDesc> {

    /**
     * Default access url.
     */
    private static final URI DEFAULT_URI = URI.create("https://broker.com");

    /**
     * Default string value.
     */
    private static final String DEFAULT_STRING = "unknown";

    /**
     * @param desc The description of the entity.
     * @return The new broker entity.
     */
    @Override
    public Broker create(BrokerDesc desc) {
        Utils.requireNonNull(desc, ErrorMessages.DESC_NULL);

        final var broker = new Broker();
        broker.setOfferedResources(new ArrayList<>());

        update(broker, desc);
        return broker;
    }

    /**
     * @param broker The entity to be updated.
     * @param desc   The description of the new entity.
     * @return True, if broker is updated.
     */
    @Override
    public boolean update(Broker broker, BrokerDesc desc) {
        Utils.requireNonNull(broker, ErrorMessages.ENTITY_NULL);
        Utils.requireNonNull(desc, ErrorMessages.DESC_NULL);

        final var newAccessUrl = updateAccessUrl(broker, broker.getAccessUrl());
        final var newTitle = updateTitle(broker, broker.getTitle());
        final var newStatus = updateRegisterStatus(broker, broker.getStatus());

        return newAccessUrl || newTitle || newStatus;
    }

    /**
     * @param broker The entity to be updated.
     * @param status The registration status of the broker.
     * @return True, if broker is updated.
     */
    private boolean updateRegisterStatus(Broker broker, RegisterStatus status) {
        final boolean updated;
        if (broker.getStatus().equals(status)) {
            updated = false;
        } else {
            broker.setStatus(status);
            updated = true;
        }
        return updated;
    }

    /**
     * @param broker The entity to be updated.
     * @param title  The new title of the entity.
     * @return True, if broker is updated
     */
    private boolean updateTitle(Broker broker, String title) {
        final var newTitle = MetadataUtils.updateString(broker.getTitle(), title,
                DEFAULT_STRING);
        newTitle.ifPresent(broker::setTitle);
        return newTitle.isPresent();
    }

    /**
     * @param broker    The entity to be updated.
     * @param accessUrl The new access url of the entity.
     * @return True, if broker is updated.
     */
    private boolean updateAccessUrl(Broker broker, URI accessUrl) {
        final var newAccessUrl = MetadataUtils.updateUri(broker.getAccessUrl(), accessUrl,
                DEFAULT_URI);
        newAccessUrl.ifPresent(broker::setAccessUrl);
        return newAccessUrl.isPresent();
    }
}
