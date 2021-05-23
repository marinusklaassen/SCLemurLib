/*
 * FILENAME: LemurClientConfig
 *
 * DESCRIPTION:
 *         Lemur Client default configuration. Overrides can be made in the startup.cs file or in a LemurClient instance.
 *
 * AUTHOR: Marinus Klaassen (2021Q2)
 *
 */

LemurClientConfig {
	classvar
	<>defaultLemurClientRemoteIP, <>widgetColor, <>editorPort, <>oscPort;

	*initClass {
		widgetColor=Color.gray;
		editorPort = 8002;
		oscPort = 8000;
	}

	*setDefaultLemurClientRemoteIP { | remoteIP |
		defaultLemurClientRemoteIP = remoteIP;
		postln("The default lemur client remote IP is set to " + remoteIP);
	}
}
