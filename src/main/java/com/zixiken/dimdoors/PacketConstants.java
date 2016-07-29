package com.zixiken.dimdoors;

public class PacketConstants
{
	private PacketConstants() { }

	public static final String CHANNEL_NAME = "DimDoorsPackets";
	
	public static final byte CLIENT_JOIN_PACKET_ID = 1;
	public static final byte CREATE_DIM_PACKET_ID = 2;
	public static final byte DELETE_DIM_PACKET_ID = 3;
	public static final byte CREATE_LINK_PACKET_ID = 4;
	public static final byte DELETE_LINK_PACKET_ID = 5;
	public static final byte CLIENT_LOGIN_DIM_REGISTER = 6;
	public static final byte UPDATE_LINK_PACKET_ID = 7;

}