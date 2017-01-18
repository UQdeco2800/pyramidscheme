package uq.deco2800.pyramidscheme.chat;

import org.junit.Assert;
import org.junit.Test;

public class ChatChannelTest {
	private final String channelId = "channelId1";
	private final String channelName = "channelName1";




	@Test
	public void testToString() {
		ChatChannel c = new ChatChannel(channelId, channelName);
		Assert.assertEquals(channelName, c.toString());
	}
}
