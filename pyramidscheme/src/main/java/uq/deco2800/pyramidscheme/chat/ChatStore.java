package uq.deco2800.pyramidscheme.chat;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Model for chat session.
 */
public class ChatStore {
    private ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
    private ObservableList<ChatChannel> channels = FXCollections.observableArrayList();

    private Map<String, ObservableList<ChatMessage>> messagesByChannelId = new HashMap<>();
    private Map<String, ChatChannel> channelsByChannelId = new HashMap<>();

    public ChatStore() {
        /*
         * Keep the channel id mappings up to date when new messages or channels are added.
		 */
        messages.addListener((ListChangeListener.Change<? extends ChatMessage> c) -> {
            while (c.next()) {
                c.getAddedSubList().forEach(m -> {
                    String channelId = m.channelId;

					/* Ensure there is a mapping for this channel */
                    messagesByChannelId.putIfAbsent(channelId, FXCollections.observableArrayList());

					/* Add new messages to the channel list */
                    ObservableList<ChatMessage> ms = messagesByChannelId.get(channelId);
                    if (!ms.contains(m)) {
                        ms.add(m);
                    }
                });
            }
        });
        channels.addListener((ListChangeListener.Change<? extends ChatChannel> c) -> {
            while (c.next()) {
                c.getAddedSubList().forEach(channel -> {
                    /* Ensure there is a mapping for this channel */
                    messagesByChannelId.putIfAbsent(channel.channelId, FXCollections.observableArrayList());
                    channelsByChannelId.putIfAbsent(channel.channelId, channel);
                });
            }
        });
    }

    /**
     * Store the message.
     *
     * @param message
     */
    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

    /**
     * Store a channel.
     *
     * @param channel
     */
    public void addChannel(ChatChannel channel) {
        channels.add(channel);
    }

    /**
     * Remove a channel.
     *
     * @param channel
     */
    public void removeChannel(ChatChannel channel) {
        channels.remove(channel);
    }

    /**
     * Find messages corresponding to the channel id.
     * <p>
     * If no such channel exists or there are no such messages, then return
     * the empty list.
     *
     * @param channelId
     * @return observation list of chat message
     */
    public ObservableList<ChatMessage> getMessagesForChannel(String channelId) {
        return messagesByChannelId.getOrDefault(channelId, FXCollections.emptyObservableList());
    }

    /**
     * Get the channels as an observable list.
     *
     * @return observation list of chat message
     */
    public ObservableList<ChatChannel> getChannels() {
        return channels;
    }

    /*
     * Get the channel corresponding to the channel id.
     * <p>
     * If no such channel exists then return empty.
     */
    public Optional<ChatChannel> getChannelById(String channelId) {
        return Optional.ofNullable(channelsByChannelId.get(channelId));
    }
}
