package com.sollyu.xposed.hook.model.utils;

import java.io.DataOutputStream;

public class RootUtil
{
	protected static void exec(String cmd)
	{
		try
		{
			Process v2 = Runtime.getRuntime().exec("su");
			DataOutputStream v1 = new DataOutputStream(v2.getOutputStream());
			v1.writeBytes(cmd);
			v1.flush();
			v1.writeBytes("exit\n");
			v1.flush();
			v2.waitFor();
		}
		catch (Exception v0)
		{
			v0.printStackTrace();
		}
	}

	public static void killProcess(String packageName)
	{
		RootUtil.exec("am force-stop " + packageName + " \n");
	}

	public static void reboot()
	{
		RootUtil.exec("reboot\n");
	}

	public static void shutdown()
	{
		RootUtil.exec("reboot -p");
	}
}
