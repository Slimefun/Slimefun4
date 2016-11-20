package me.mrCookieSlime.Slimefun.api.item_transport;

public class ChannelIndex {
	
	public int channel;
	public int index;
	
	public ChannelIndex(int channel, int index) {
		this.channel = channel;
		this.index = index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getChannel() {
		return this.channel;
	}

	public int getIndex() {
		return this.index;
	}

}
