package io.dataspaceconnector.model;

import io.dataspaceconnector.utils.ErrorMessages;
import io.dataspaceconnector.utils.MetadataUtils;
import io.dataspaceconnector.utils.Utils;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Creates and updates an app endpoint.
 */
@Component
public class AppEndpointFactory extends EndpointFactory<AppEndpoint, AppEndpointDesc> {

    /**
     * The default access url.
     */
    public static final String DEFAULT_ACCESS_URL = "https://default";

    /**
     *
     */
    public static final String DEFAULT_STRING = "unknown";

    /**
     * @param appEndpoint The app endpoint
     * @param desc        The description of the new entity.
     * @return true, if app endpoint is updated
     */
    @Override
    public boolean update(final AppEndpoint appEndpoint, AppEndpointDesc desc) {
        Utils.requireNonNull(appEndpoint, ErrorMessages.ENTITY_NULL);
        Utils.requireNonNull(desc, ErrorMessages.DESC_NULL);

        final var hasUpdatedAccessUrl = updateAccessUrl(appEndpoint, desc.getAccessURL());
        final var hasUpdatedMediaType = updateMediaType(appEndpoint, desc.getMediaType());
        final var hasUpdatedPort = updateEndpointPort(appEndpoint, appEndpoint.getAppEndpointPort());
        final var hasUpdatedProtocol = updateProtocol(appEndpoint, appEndpoint.getAppEndpointProtocol());
        final var hasUpdatedLanguage = updateLanguage(appEndpoint, appEndpoint.getLanguage());

        return hasUpdatedAccessUrl || hasUpdatedMediaType || hasUpdatedPort || hasUpdatedProtocol || hasUpdatedLanguage;
    }

    /**
     * @param appEndpoint The app endpoint
     * @param language    The app endpoint protocol
     * @return true, if language is updated
     */
    private boolean updateLanguage(AppEndpoint appEndpoint, String language) {
        final var newLanguage = MetadataUtils.updateString(appEndpoint.getAppEndpointProtocol(),
                language, "EN");
        newLanguage.ifPresent(appEndpoint::setLanguage);
        return newLanguage.isPresent();
    }

    /**
     * @param appEndpoint         The app endpoint
     * @param appEndpointProtocol The app endpoint protocol
     * @return true, if protocol is updated
     */
    private boolean updateProtocol(AppEndpoint appEndpoint, String appEndpointProtocol) {
        final var newProtocol = MetadataUtils.updateString(appEndpoint.getAppEndpointProtocol(),
                appEndpointProtocol, DEFAULT_STRING);
        newProtocol.ifPresent(appEndpoint::setAppEndpointProtocol);
        return newProtocol.isPresent();
    }

    /**
     * @param appEndpoint     The app endpoint
     * @param appEndpointPort The new app endpoint port
     * @return true, if app endpoint is updated
     */
    private boolean updateEndpointPort(AppEndpoint appEndpoint, int appEndpointPort) {

        final var newPort = MetadataUtils.updateInteger(appEndpoint.getAppEndpointPort(),
                appEndpointPort);
        newPort.ifPresent(appEndpoint::setAppEndpointPort);
        return newPort.isPresent();
    }

    /**
     * @param appEndpoint The app endpoint
     * @param mediaType   The new media type
     * @return true, if media type is updated
     */
    private boolean updateMediaType(AppEndpoint appEndpoint, String mediaType) {
        final var newMediaType = MetadataUtils.updateString(appEndpoint.getMediaType(), mediaType,
                DEFAULT_STRING);
        newMediaType.ifPresent(appEndpoint::setMediaType);
        return newMediaType.isPresent();
    }

    /**
     * @param appEndpoint The app endpoint
     * @param accessURL   The new access url
     * @return true, if access url is updated
     */
    private boolean updateAccessUrl(AppEndpoint appEndpoint, URI accessURL) {
        final var newUri = MetadataUtils.updateUri(appEndpoint.getAccessURL(), accessURL,
                URI.create(DEFAULT_ACCESS_URL));

        newUri.ifPresent(appEndpoint::setAccessURL);
        return newUri.isPresent();
    }

    /**
     * @param desc The description passed to the factory.
     * @return app endpoint
     */
    @Override
    protected AppEndpoint createInternal(final AppEndpointDesc desc) {
        Utils.requireNonNull(desc, ErrorMessages.DESC_NULL);

        final var appEndpoint = new AppEndpoint();

        update(appEndpoint, desc);
        return appEndpoint;
    }
}
