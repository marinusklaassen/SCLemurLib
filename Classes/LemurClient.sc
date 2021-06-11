/*
 * FILENAME: LemurClient
 *
 * DESCRIPTION:
 *         Lemur client logic for building user interfaces with the Lemur iOS app without the need of a Lemur Editor.
 *         - Editing/build an interfaces.
 *         - Respond to OSC messages
 *
 * AUTHOR: Marinus Klaassen (2012, 2021Q2)
 *
 */

LemurClient {
	classvar <>postXMLRequestsToConsole=false;
	var <lemurConnection;

	*new { ^super.new; }

	connect { | remoteIP |
		lemurConnection = LemurConnectionPool.connect(remoteIP);
	}

	disconnect {
		if (lemurConnection.notNil, {
			LemurConnectionPool.disconnect(lemurConnection);
		}, {
		    postln("Cannot disconnect because the Lemur connection first has to be established.");
		});
	}

	sendRequest { | message |
	    if (lemurConnection.notNil, {
			message.indent = 0;
			message.preserveWhitespace = false;
			message = message.format();
			if (postXMLRequestsToConsole, {
			  postln(format("Send XML Request to Lemur -> %", message)); });
			message = message.ascii.add(0).as(Int8Array);
			lemurConnection.uiEditorNetAddr.sendMsg("/jzml", message);
		    },{
		    postln("Cannot send an UI editing message because there's no connection made.");
		});
	}

	reset {
		this.sendRequest(LemurClientRequestBodyComposer.getResetAllRequest());
	}

	page { |pagename, x, y, width, height |
		var dictOverrideAttributes = Dictionary.newFrom([\text, pagename, \x, x, \x, y, \width, width, \height, height]);
		this.sendRequest(LemurClientRequestBodyComposer.getPageAddRequest(dictOverrideAttributes));
    }

	renamePage { | newname, oldname  |
		this.sendRequest(LemurClientRequestBodyComposer.getPageRenameRequest(newname, oldname));
	}

	removePage { | pagename |
		this.sendRequest(LemurClientRequestBodyComposer.getPageRemoveRequest(pagename));
	}

	fader { | pagename, id, x, y, width, height, color |
	    var nodeAttributeOverrides = Dictionary.newFrom([\class, \FADER, \text, id, \x, x, \y, y, \width, width, \height, height, \color, color]);
		this.sendRequest(LemurClientRequestBodyComposer.getAddWidgetRequest(pagename, nodeAttributeOverrides));
	}

	removeFader { | pagename, id |
		this.sendRequest(LemurClientRequestBodyComposer.getRemoveWidgetRequest("Fader", pagename, id));
	}

	range { | pagename, id, x, y, width, height, color |
		var nodeAttributeOverrides = Dictionary.newFrom([\class, \RANGE, \text, id, \x, x, \y, y, \width, width, \height, height, \color, color]);
		this.sendRequest(LemurClientRequestBodyComposer.getAddWidgetRequest(pagename, nodeAttributeOverrides));
	}

	removeRange { | pagename, id |
		this.sendRequest(LemurClientRequestBodyComposer.getRemoveWidgetRequest("RANGE", pagename, id));
	}

	text { | pagename, id, content, x, y, width, height, color |
	    var nodeAttributeOverrides = Dictionary.newFrom([\class, \Text, \text, id, \content, content, \x, x, \y, y, \width, width, \height, height, \color, color]);
		this.sendRequest(LemurClientRequestBodyComposer.getAddWidgetRequest(pagename, nodeAttributeOverrides));
	}

	removeText { |pagename, id |
		this.sendRequest(LemurClientRequestBodyComposer.getRemoveWidgetRequest("Text", pagename, id));
	}

	pads { |pagename, id, x, y, width, height, columnSize, rowSize|
	    var nodeAttributeOverrides = Dictionary.newFrom([\class, \Pads, \text, id, \x, x, \y, y, \width, width, \height, height, \row, rowSize, \column, columnSize]);
		this.sendRequest(LemurClientRequestBodyComposer.getAddWidgetRequest(pagename, nodeAttributeOverrides));
	}

	removePads { |pagename, id |
		this.sendRequest(LemurClientRequestBodyComposer.getRemoveWidgetRequest("Pads", pagename, id));
	}

	switches { |pagename, id, x, y, width, height, columnSize, rowSize |
	     var nodeAttributeOverrides = Dictionary.newFrom([\class, \Switches, \text, id, \x, x, \y, y, \width, width, \height, height, \column, columnSize, \row, rowSize]);
		this.sendRequest(LemurClientRequestBodyComposer.getAddWidgetRequest(pagename, nodeAttributeOverrides));
	}

	removeSwitches { | pagename, id |
		this.sendRequest(LemurClientRequestBodyComposer.getRemoveWidgetRequest("Switches", pagename, id));
	}

	setOscTarget { | targetId, targetIP, targetPort |
		var nodeAttributeOverrides = Dictionary.newFrom([\target, targetId, \ip, targetIP, \port, targetPort]);
		this.sendRequest(LemurClientRequestBodyComposer.getOscTargetRequest(nodeAttributeOverrides));
	}
}