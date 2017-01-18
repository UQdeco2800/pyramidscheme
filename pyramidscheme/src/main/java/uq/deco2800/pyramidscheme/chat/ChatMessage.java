package uq.deco2800.pyramidscheme.chat;

import uq.deco2800.singularity.common.representations.realtime.IncomingMessage;

public class ChatMessage {
    public final String message;
    public final String channelId;
    /* needed for determining if the author is the current user for styling */
    public final String selfUsername;
    public final String authorUserId;
    public final String authorUsername;

    ChatMessage(String message, String channelId, String selfUsername, String authorUserId, String authorUsername) {
        this.message = message;
        this.channelId = channelId;
        this.selfUsername = selfUsername;
        this.authorUserId = authorUserId;
        this.authorUsername = authorUsername;
    }

    /**
     * get chat message
     *
     * @param incoming message, user name
     * @return
     **/
    public ChatMessage(IncomingMessage incoming, String selfUsername) {
        this(
                incoming.getMessage(),
                incoming.getToMessageChannelId(),
                selfUsername,
                incoming.getFromUserId(),
                incoming.getFromUserName()
        );
    }
}
