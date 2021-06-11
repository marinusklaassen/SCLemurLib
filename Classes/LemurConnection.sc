/*
 * FILENAME: LemurConnection
 *
 * DESCRIPTION:
 *         Lemur connection: There are 2 messages possible. Editing messages via TCP and OSC messages via UDP.
 *         Using this class you have 1 entry point to the server ports the Lemur app facilitaties.
 *         An instances is create and managed by the LemurConnectionPool. The LemurClient talks via a
 *         LemurConnection instance to the remote Lemur app.
 *
 *         So far I know the Lemur ports are fixed. However when this is not the case your can override
 *         the default classvars, like I would advice you should do with the defaultRemoteIP, in the
 *         Startup.cs like:
 *         LemurConnection.defaultRemoteIP = "192.168.2.11";
 *
 * AUTHOR: Marinus Klaassen (2021Q2)
 *
 */

LemurConnection {
	classvar <>defaultEditorPort=8002, <>defaultOscPort=8000, <>defaultRemoteIP;
    var <>remoteIP, <oscNetAddr, <uiEditorNetAddr, <>editorPort, <>oscPort;

	*new { | remoteIP |
		if (remoteIP.isNil) {
			throw(Error("The RemoteIP constructor argument is not provided."));
		}
		// newCopyArgs works sets properties when they are defined in the order from left to right.
		^super.newCopyArgs(remoteIP).init();
	}

	init {
		editorPort = defaultEditorPort;
		oscPort = defaultOscPort;
		uiEditorNetAddr = NetAddr(remoteIP, editorPort);
		oscNetAddr = NetAddr(remoteIP, oscPort);
	}

	connect {
		uiEditorNetAddr.connect();
		// oscNetAddr no need to connect because of UDP.
	}

	disconnect {
		uiEditorNetAddr.disconnect();
		oscNetAddr.disconnect();
	}
}
