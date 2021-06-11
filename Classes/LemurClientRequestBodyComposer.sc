/*
* FILENAME: LemurClientRequestBodyComposer
*
* DESCRIPTION:
*         Builds/composes Lemur XML dom elements that the Lemur IOS app can understand.
*         - Works with defaults that be overriden in the startup file or while calling the build methods.
*
* NOTES:
*         All build methods return a DOM where all the corresponding elements and attributes are set.
*         Color option. Lemur manual page 129 gives the formular to convert rgb values with with values between 0.00 - 1.00 to a color range of 0 8355711.
*
* AUTHOR: Marinus Klaassen (2021Q2)
*
*/

LemurClientRequestBodyComposer {
	classvar defaults, domElementFactory, elementMapping;

	*initClass {
        domElementFactory = DOMDocument();
		elementMapping = Dictionary();
	    this.resetDefaults();
	}

	*resetDefaults {
		defaults = MultiLevelIdentityDictionary();

		defaults[\PROJECT, \version]        = 3030;
		defaults[\PROJECT, \title]          = "";
		defaults[\PROJECT, \width]          = 1024;
		defaults[\PROJECT, \height]         = 724;
		defaults[\PROJECT, \osc_target]     = -2;
		defaults[\PROJECT, \midi_target]    = -2;
		defaults[\PROJECT, \kbmouse_target] = -2;

		defaults[\OSC, \request] = 1;

		defaults[\SYNCHRO, \mode] = 0;

		defaults[\PAGE, \class] = "JAZZINTERFACE";
		defaults[\PAGE, \text] = "";
		defaults[\PAGE, \x] = 0;
		defaults[\PAGE, \y] = 0;
		defaults[\PAGE, \width] = 1024;
		defaults[\PAGE, \height] = 724;
		defaults[\PAGE, \state] = 1;
		defaults[\PAGE, \group] = 0;
		defaults[\PAGE, \font] = "tahoma,11,0";

		defaults[\PAGE_REMOVE, \group] = 0;
		defaults[\PAGE_REMOVE, \class] = defaults[\PAGE, \class];

		defaults[\PAGE_RENAME, \class] = defaults[\PAGE, \class];

		// Widget Base
		defaults[\WIDGET_BASE, \class] = "";
		defaults[\WIDGET_BASE, \text] = "";
		defaults[\WIDGET_BASE, \x] = 0;
		defaults[\WIDGET_BASE, \y] = 0;
		defaults[\WIDGET_BASE, \width] =  100;
	    defaults[\WIDGET_BASE, \height] = 724;
		defaults[\WIDGET_BASE, \id] = 1;
		defaults[\WIDGET_BASE, \state] = 1;
		defaults[\WIDGET_BASE, \group] = 0;
		defaults[\WIDGET_BASE, \font] = "tahoma,11,0";
		defaults[\WIDGET_BASE, \send] = 1;
		defaults[\WIDGET_BASE, \osc_target] = -2;
		defaults[\WIDGET_BASE, \midi_target] = -2;
		defaults[\WIDGET_BASE, \kbmouse_target] = -2;
	    defaults[\WIDGET_BASE, \color] = Color.yellow;
		defaults[\WIDGET_BASE, \label] = 0;

		// Slider Base
		defaults[\FADER_BASE, \PARENT] = \WIDGET_BASE;
		defaults[\FADER_BASE, \capture] = 1;
		defaults[\FADER_BASE, \cursor] =  0;
	    defaults[\FADER_BASE, \grid] = 0;
		defaults[\FADER_BASE, \grid_steps] = 1;
		defaults[\FADER_BASE, \physics] = 0;
		defaults[\FADER_BASE, \precision] = 3;
		defaults[\FADER_BASE, \unit] = "";
		defaults[\FADER_BASE, \value] = 0;
		defaults[\FADER_BASE, \zoom] = "0.000000";
		defaults[\FADER_BASE, \kbm] = -2;
		defaults[\FADER_BASE, \capture] = 1;
		defaults[\FADER_BASE, \horizontal] = 0;

		// Slider
		defaults[\FADER, \PARENT] = \FADER_BASE;

		// Range
		defaults[\RANGE, \PARENT] = \FADER_BASE;
		defaults[\RANGE, \value] = "0.000000,1.000000";

		// Text
		defaults[\Text, \PARENT] = \WIDGET_BASE;
		defaults[\Text, \context] = "Text";

		// Pads/Switches base
		defaults[\PRESS_BASE, \PARENT] = \WIDGET_BASE;
		defaults[\PRESS_BASE, \color] = "%,%".format(this.convertColorToInt(Color.blue), this.convertColorToInt(Color.red));
		defaults[\PRESS_BASE, \multilabel] = 1;
		defaults[\PRESS_BASE, \row] = 1;
		defaults[\PRESS_BASE, \column] = 1;
		defaults[\PRESS_BASE, \radio] = 0;

		// Pads
	    defaults[\Pads, \PARENT] = \PRESS_BASE;
		defaults[\Pads, \state] = 245;

	    // Switches
		defaults[\Switches, \PARENT] = \PRESS_BASE;
	}

	*getDefaultsAttributes { | defaultSection |
        var resultDict;
		var parent = defaults[defaultSection, \PARENT];
		if (parent.notNil, {
			resultDict = this.getDefaultsAttributes(parent); // recursive call because an section can inherit attributes from other section.
		}, {
			resultDict = IdentityDictionary();
		});
		if (defaults[defaultSection].notNil, {
			defaults[defaultSection].keys do: { | attributeName |
			if (attributeName != \PARENT, { resultDict[attributeName] = defaults[defaultSection, attributeName]; });
		}});
		^resultDict;
	}

	*overrideDefaultAttribute { | elementName, attributeName, value |
		// var defaultAttributes = this.getDefaultsAttributes
		if (defaults[elementName, elementName].isNil, {
			postln(format("LemurClientRequestBuilder default config override setting for element '%' and attribute '%' does not exist! The available options are:", elementName, attributeName));
			defaults.postTree();
		},
		{
			defaults[elementName, attributeName] = value;
			postln(format("LemurClientRequestBuilder default config element '%' and attribute '%' is overridden with value %", elementName, attributeName, value));
		});
	}

	*composeDOMElement { | elementName, dictConfigSection, dictOverrideSettings |
		var newDOMElement, configSectionKey;
		newDOMElement = domElementFactory.createElement(elementName);
		if (dictConfigSection.notNil, {
			newDOMElement = this.setAttributesByDictionary(newDOMElement, dictConfigSection);
		});
		if (dictOverrideSettings.notNil, {
			newDOMElement = this.setAttributesByDictionary(newDOMElement, dictOverrideSettings);
	    });
		^newDOMElement;
	}

	// Aan het einde van het project een DOMElement extension method maken.
	*setAttributesByDictionary{ | domElement, dictAttributeSettings |
		dictAttributeSettings.keys do: { | key |
			var value = dictAttributeSettings[key];
			value = if (value.isKindOf(Color), { this.convertColorToInt(value); }, { value; });
			domElement.setAttribute(key, value.asString);
		};
		^domElement;
	}

	*convertColorToInt { | color |
	   var colorArray, colorInt;
		colorArray = color.asArray.copyRange(0,2).round(0.01).clip(0,1.0);
		colorInt = this.convertColorRGBToColorInt(*colorArray);
		^colorInt;
	}

	*convertColorRGBToColorInt { |r, g, b|
		var colorInt = (r * 127 * 65536) + (g * 127 * 256) + (b * 127);
		^colorInt.asInteger;
	}

	*getResetAllRequest {
		var xmlDoc = DOMDocument();
		[\RESET, \PROJECT, \SYNCHRO, \OSC] do: { |elementName|
			xmlDoc.appendChild(this.composeDOMElement(elementName, defaults[elementName]));
		};
		^xmlDoc;
	}

	*getPageAddRequest { | nodeAttributeOverrides |
		var xmlDoc = DOMDocument();
		xmlDoc.appendChild(this.composeDOMElement("WINDOW", defaults[\PAGE], nodeAttributeOverrides)); // A WINDOW tag will target a page.
		^xmlDoc;
	}

	*getPageRenameRequest {  | oldName, newName |
		var xmlDoc = DOMDocument();
		xmlDoc.appendChild(
			this.composeDOMElement("RENAME").setAttribute("text", newName)
			.appendChild(this.composeDOMElement("WINDOW", defaults[\PAGE_RENAME]).setAttribute("text", oldName))); // A WINDOW tag will target a page.
		^xmlDoc;
	}

	*getPageRemoveRequest { | pagename |
		var xmlDoc = DOMDocument();
		xmlDoc.appendChild(
			this.composeDOMElement("DELETE")
			.appendChild(this.composeDOMElement("WINDOW", defaults[\PAGE_REMOVE]).setAttribute("text", pagename)); // A WINDOW tag will target a page.
		);
		^xmlDoc;
	}

	*getAddWidgetRequest { | pagename, nodeAttributeOverrides |
    	var xmlDoc = DOMDocument();
		var elementTargetPage = this.composeDOMElement("WINDOW") // This element targets a page or widget
		.setAttribute("class", "JAZZINTERFACE")
		.setAttribute("text", pagename);
		var elementWidget = this.composeDOMElement("WINDOW", this.getDefaultsAttributes(nodeAttributeOverrides[\class]), nodeAttributeOverrides);
	    xmlDoc.appendChild(elementTargetPage);
	    elementTargetPage.appendChild(elementWidget);
		^xmlDoc;
	}

	*getRemoveWidgetRequest { | class, pagename, id |
	   	var xmlDoc = DOMDocument();
		// Set the node and attributes
		var targetPageNode = this.composeDOMElement("WINDOW") // This element targets a page or widget
		.setAttribute("class", "JAZZINTERFACE")
		.setAttribute("text", pagename);
	    var deleteNode = this.composeDOMElement("DELETE");
		var targetWidgetNode = this.composeDOMElement("WINDOW")
		.setAttribute("class", class.asString())
		.setAttribute("text", id.asString());
		// Create the dom tree
		xmlDoc.appendChild(targetPageNode);
		targetPageNode.appendChild(deleteNode);
		deleteNode.appendChild(targetWidgetNode);
		^xmlDoc;
	}

	*getOscTargetRequest { | nodeAttributeOverrides |
		var xmlDoc = DOMDocument();
	    xmlDoc.appendChild(this.composeDOMElement(\OSC, nodeAttributeOverrides));
		^xmlDoc;
	}
}

