/*
 * FILENAME: LemurConnection
 *
 * DESCRIPTION:
 *         Lemur connection: There are 2 messages possible. Editing via TCP and OSC messages via UDP.
 *         Using this class you have 1 entry point to the connections.
 *
 * AUTHOR: Marinus Klaassen (2021Q2)
 *
 */

LemurConnection {
	var <>remoteIP, <oscNetAddr, <uiEditorNetAddr, <>editorPort, <>oscPort;

	*new { | remoteIP |
		if (remoteIP.isNil) {
			throw(Error("The RemoteIP constructor argument is not provided."));
		}
		^super.newCopyArgs(remoteIP).init(); /* newCopyArgs works sets properties when they are defined in the order from left to right. */
	}

	init {
		editorPort = LemurClientConfig.editorPort;
		oscPort = LemurClientConfig.oscPort;
		uiEditorNetAddr = NetAddr(remoteIP, editorPort);
		oscNetAddr = NetAddr(remoteIP, oscPort);
	}

	connect {
		uiEditorNetAddr.connect();
	}

	disconnect {
		uiEditorNetAddr.disconnect();
		oscNetAddr.disconnect();
	}
}
