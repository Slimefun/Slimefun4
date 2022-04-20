package io.github.thebusybiscuit.slimefun4.api.network;

import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;

/**
 * This enum holds the different types of components a {@link Network} can have.
 * It is used for classification of nodes inside the {@link Network}.
 * 
 * @author meiamsome
 * 
 * @see Network
 * @see NetworkManager
 *
 */
public enum NetworkComponent {

    /**
     * This represents a simple connector node.
     */
    CONNECTOR,

    /**
     * This represents the main component of the {@link Network}.
     * This node is responsible for managing the {@link Network}.
     */
    REGULATOR,

    /**
     * This represents an endpoint of a {@link Network}.
     * This endpoint can either be a source or a destination.
     */
    TERMINUS;

}