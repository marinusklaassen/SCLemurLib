/*
 * FILENAME: LemurConnectionPool
 *
 * DESCRIPTION:
 *         Lemur connection pool logic for connecting with the Lemur iOS app.
 *         - Re-use or setup a new LemurConnection.
 *
 *
 * AUTHOR: Marinus Klaassen (2012, 2021Q2)
 *
 */

LemurConnectionPool {
	classvar <>connections;

    *initClass {
		connections = IdentityDictionary();
	}

	*connect { | remoteIP |

		var lemurConnection;

	    if (remoteIP.isNil, {
	        remoteIP = LemurClientConfig.defaultLemurClientRemoteIP;
			postln(format("Remote IP is taken from the default remote IP '%'", remoteIP));
		});

		if (remoteIP.isNil, {
			postln("Warning! No remoteIP method argument is provided or LemurClientConfig.defaultLemurClientRemoteIP configured!");
			^nil;
	    });

		// Multiple LemurClient instances can use the same client endpoint. Create a new connection if needed.
		if (connections[remoteIP] == nil, {
			lemurConnection = LemurConnection(remoteIP);
			lemurConnection.connect();
		    postln(format("Connecting LemurClient remote IP '%', buildPort=%, oscPort=%.", lemurConnection.remoteIP, lemurConnection.editorPort, lemurConnection.oscPort));
			postln("New connection succeeded.");
			connections[remoteIP] = lemurConnection;
		}, {
			lemurConnection = connections[remoteIP];
			postln("Reusing an existing connection for remote IP " + remoteIP);
		});
		postln(connections);
		^lemurConnection;
	}

	*disconnect { | lemurConnection |
		if (connections[lemurConnection.remoteIP].notNil, {

		  postln(format("Disconnecting LemurClient remote IP '%', buildPort=%, oscPort=%.", lemurConnection.remoteIP, lemurConnection.editorPort, lemurConnection.oscPort));
			connections[lemurConnection.remoteIP].disconnect();
			connections[lemurConnection.remoteIP] = nil;
		}, {
			postln(format("Could not disconnection because there is no connection is available for remote IP '%'.", lemurConnection.remoteIP));
		});
	}
}
